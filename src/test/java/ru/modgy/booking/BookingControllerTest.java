package ru.modgy.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.modgy.booking.controller.BookingController;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.model.StatusBooking;
import ru.modgy.booking.model.TypesBooking;
import ru.modgy.booking.service.BookingService;
import ru.modgy.exception.NotFoundException;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.dto.RoomDto;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    private final String requesterHeader = "X-PetHotel-User-Id";
    private final UpdateBookingDto updateBookingDto = UpdateBookingDto.builder()
            .isPrepaid(true)
            .status(StatusBooking.STATUS_CANCELLED)
            .build();
    long requesterId = 1L;
    long bookingId = 1L;
    long roomId = 1L;
    private final RoomDto roomDto = RoomDto.builder()
            .id(roomId)
            .area(5.0)
            .number("standard room")
            .categoryDto(new CategoryDto(1L, "name", "description"))
            .isVisible(true)
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(bookingId)
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
            .status(StatusBooking.STATUS_INITIAL)
            .isPrepaid(false)
            .room(roomDto)
            .price(0.0)
            .amount(0.0)
            .prepaymentAmount(0.0)
            .build();
    private final NewBookingDto newBookingDto = NewBookingDto.builder()
            .type(TypesBooking.TYPE_BOOKING)
            .checkInDate(LocalDate.now())
            .checkOutDate(LocalDate.now().plusDays(7))
            .isPrepaid(false)
            .roomId(roomId)
            .build();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    @Test
    @SneakyThrows
    void addBooking() {
        when(bookingService.addBooking(anyLong(), any(NewBookingDto.class))).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newBookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.type", is(bookingDto.getType().toString())))
                .andExpect(jsonPath("$.checkInDate", is(bookingDto.getCheckInDate().toString())))
                .andExpect(jsonPath("$.checkOutDate", is(bookingDto.getCheckOutDate().toString())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.isPrepaid", is(bookingDto.getIsPrepaid())))
                .andExpect(jsonPath("$.room").value(bookingDto.getRoom()))
                .andExpect(jsonPath("$.price").value(bookingDto.getPrice()))
                .andExpect(jsonPath("$.amount").value(bookingDto.getAmount()))
                .andExpect(jsonPath("$.prepaymentAmount").value(bookingDto.getPrepaymentAmount()));

        verify(bookingService).addBooking(anyLong(), any(NewBookingDto.class));

        mockMvc.perform(post("/bookings")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newBookingDto)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/bookings")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new BookingDto())))
                .andExpect(status().isBadRequest());

        verify(bookingService, times(1)).addBooking(anyLong(), any(NewBookingDto.class));
    }

    @Test
    @SneakyThrows
    void getBookingById() {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{id}", bookingId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.type", is(bookingDto.getType().toString())))
                .andExpect(jsonPath("$.checkInDate", is(bookingDto.getCheckInDate().toString())))
                .andExpect(jsonPath("$.checkOutDate", is(bookingDto.getCheckOutDate().toString())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.isPrepaid", is(bookingDto.getIsPrepaid())))
                .andExpect(jsonPath("$.room").value(bookingDto.getRoom()))
                .andExpect(jsonPath("$.price").value(bookingDto.getPrice()))
                .andExpect(jsonPath("$.amount").value(bookingDto.getAmount()))
                .andExpect(jsonPath("$.prepaymentAmount").value(bookingDto.getPrepaymentAmount()));

        verify(bookingService).getBookingById(requesterId, bookingId);

        when(bookingService.getBookingById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/bookings/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(bookingService, times(2)).getBookingById(requesterId, bookingId);
    }

    @Test
    @SneakyThrows
    void updateBooking() {
        when(bookingService.updateBooking(anyLong(), eq(bookingId), any(UpdateBookingDto.class))).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateBookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.type", is(bookingDto.getType().toString())))
                .andExpect(jsonPath("$.checkInDate", is(bookingDto.getCheckInDate().toString())))
                .andExpect(jsonPath("$.checkOutDate", is(bookingDto.getCheckOutDate().toString())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())))
                .andExpect(jsonPath("$.isPrepaid", is(bookingDto.getIsPrepaid())))
                .andExpect(jsonPath("$.room").value(bookingDto.getRoom()))
                .andExpect(jsonPath("$.price").value(bookingDto.getPrice()))
                .andExpect(jsonPath("$.amount").value(bookingDto.getAmount()))
                .andExpect(jsonPath("$.prepaymentAmount").value(bookingDto.getPrepaymentAmount()));


        when(bookingService.updateBooking(anyLong(), eq(bookingId), any(UpdateBookingDto.class)))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/bookings/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateBookingDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void deleteBookingById() {
        mockMvc.perform(delete("/bookings/{Id}", bookingId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNoContent());

        verify(bookingService).deleteBookingById(requesterId, bookingId);

        doThrow(NotFoundException.class)
                .when(bookingService)
                .deleteBookingById(requesterId, bookingId);

        mockMvc.perform(delete("/bookings/{Id}", bookingId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNotFound());

        verify(bookingService, times(2)).deleteBookingById(requesterId, bookingId);
    }
}
