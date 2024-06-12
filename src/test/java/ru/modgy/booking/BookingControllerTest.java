package ru.modgy.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.modgy.booking.controller.BookingController;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.model.StatusBooking;
import ru.modgy.booking.model.TypesBooking;
import ru.modgy.booking.service.BookingService;
import ru.modgy.exception.ConflictException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.utility.UtilityService;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    private final String requesterHeader = UtilityService.REQUESTER_ID_HEADER;
    private final UpdateBookingDto updateBookingDto = UpdateBookingDto.builder()
            .isPrepaid(true)
            .status(StatusBooking.STATUS_CANCELLED)
            .build();
    private final long requesterId = 1L;
    private final long bookingId = 1L;
    private final long roomId = 1L;
    private final LocalDate checkIn = LocalDate.of(2024, 1, 1);
    private final LocalDate checkOut = LocalDate.of(2024, 1, 2);

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
    @MockBean
    private UtilityService utilityService;

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

    @Test
    @SneakyThrows
    void checkRoomAvailableInDates() {
        mockMvc.perform(get("/bookings/{roomId}/checkRoomAvailable", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .param("checkInDate", "01.01.2024" )
                        .param("checkOutDate", "02.01.2024"))
                .andExpect(status().isOk());

        verify(bookingService).checkRoomAvailableInDates(requesterId, roomId, checkIn, checkOut);

        doThrow(ConflictException.class)
                .when(bookingService)
                .checkRoomAvailableInDates(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class));

        mockMvc.perform(get("/bookings/{roomId}/checkRoomAvailable", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("checkInDate", "01.01.2024" )
                        .param("checkOutDate", "02.01.2024"))
                .andExpect(status().isConflict());

        verify(bookingService, times(2))
                .checkRoomAvailableInDates(requesterId, roomId, checkIn, checkOut);
    }

    @Test
    @SneakyThrows
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    void findBlockingBookingsForRoomInDates() {
        when(bookingService.findBlockingBookingsForRoomInDates(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/{roomId}/blockingBookingsInDates", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .param("checkInDate", "01.01.2024" )
                        .param("checkOutDate", "02.01.2024"))
                .andExpect(status().isOk());

        verify(bookingService).findBlockingBookingsForRoomInDates(requesterId, roomId, checkIn, checkOut);
        verify(bookingService, times(1))
                .findBlockingBookingsForRoomInDates(requesterId, roomId, checkIn, checkOut);
    }

    @Test
    @SneakyThrows
    void findCrossingBookingsForRoomInDates() {
        when(bookingService.findCrossingBookingsForRoomInDates(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/{roomId}/crossingBookingsOfRoomInDates", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .param("checkInDate", "01.01.2024" )
                        .param("checkOutDate", "02.01.2024"))
                .andExpect(status().isOk());

        verify(bookingService).findCrossingBookingsForRoomInDates(requesterId, roomId, checkIn, checkOut);
        verify(bookingService, times(1))
                .findCrossingBookingsForRoomInDates(requesterId, roomId, checkIn, checkOut);
    }
}
