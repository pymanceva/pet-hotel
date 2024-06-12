package ru.modgy.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.modgy.exception.NotFoundException;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.dto.NewRoomDto;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.dto.UpdateRoomDto;
import ru.modgy.room.service.RoomService;
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

@WebMvcTest(controllers = RoomController.class)
class RoomControllerIntegrationTest {
    private final String requesterHeader = "X-PetHotel-User-Id";
    private final long requesterId = 1L;
    private final long roomId = 1L;
    private final long catId = 1L;
    private final LocalDate checkIn = LocalDate.of(2024, 1, 1);
    private final LocalDate checkOut = LocalDate.of(2024, 1, 2);
    private final RoomDto roomDto = RoomDto.builder()
            .id(roomId)
            .area(5.0)
            .number("standard room")
            .categoryDto(new CategoryDto(catId, "name", "description"))
            .isVisible(true)
            .build();
    private final NewRoomDto newRoomDto = NewRoomDto.builder()
            .area(5.0)
            .number("standard room")
            .categoryId(catId)
            .isVisible(true)
            .build();
    private final UpdateRoomDto updateRoomDto = UpdateRoomDto.builder()
            .area(10.0)
            .number("updated room")
            .categoryId(catId)
            .build();
    private final RoomDto hiddenRoomDto = RoomDto.builder()
            .id(roomId)
            .area(5.0)
            .number("standard room")
            .categoryDto(new CategoryDto(catId, "name", "description"))
            .isVisible(false)
            .build();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoomService roomService;
    @MockBean
    private UtilityService utilityService;

    @Test
    @SneakyThrows
    void addRoom() {
        when(roomService.addRoom(anyLong(), any(NewRoomDto.class))).thenReturn(roomDto);

        mockMvc.perform(post("/rooms")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newRoomDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.area", is(roomDto.getArea()), Double.class))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.categoryDto").value(roomDto.getCategoryDto()))
                .andExpect(jsonPath("$.isVisible", is(roomDto.getIsVisible())));

        verify(roomService).addRoom(anyLong(), any(NewRoomDto.class));

        mockMvc.perform(post("/rooms")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newRoomDto)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/rooms")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new RoomDto())))
                .andExpect(status().isBadRequest());

        verify(roomService, times(1)).addRoom(anyLong(), any(NewRoomDto.class));
    }

    @Test
    @SneakyThrows
    void getRoomById() {
        when(roomService.getRoomById(anyLong(), anyLong())).thenReturn(roomDto);

        mockMvc.perform(get("/rooms/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.area", is(roomDto.getArea()), Double.class))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.categoryDto").value(roomDto.getCategoryDto()))
                .andExpect(jsonPath("$.isVisible", is(roomDto.getIsVisible())));

        verify(roomService).getRoomById(requesterId, roomId);

        when(roomService.getRoomById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/rooms/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(roomService, times(2)).getRoomById(requesterId, roomId);
    }

    @Test
    @SneakyThrows
    void updateRoom() {
        when(roomService.updateRoom(anyLong(), eq(roomId), any(UpdateRoomDto.class))).thenReturn(roomDto);

        mockMvc.perform(patch("/rooms/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateRoomDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.area", is(roomDto.getArea()), Double.class))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.categoryDto").value(roomDto.getCategoryDto()))
                .andExpect(jsonPath("$.isVisible", is(roomDto.getIsVisible())));


        when(roomService.updateRoom(anyLong(), eq(roomId), any(UpdateRoomDto.class)))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/rooms/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateRoomDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void getAllRooms() {
        when(roomService.getAllRooms(anyLong(), anyBoolean())).thenReturn(List.of(roomDto));

        mockMvc.perform(get("/rooms")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("isVisible", "true"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].area", is(roomDto.getArea()), Double.class))
                .andExpect(jsonPath("$.[0].number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.[0].categoryDto").value(roomDto.getCategoryDto()))
                .andExpect(jsonPath("$.[0].isVisible", is(roomDto.getIsVisible())));
    }

    @Test
    @SneakyThrows
    void hideRoomById() {
        when(roomService.hideRoomById(anyLong(), eq(roomId))).thenReturn(hiddenRoomDto);

        mockMvc.perform(patch("/rooms/{id}/hide", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(hiddenRoomDto.getId()), Long.class))
                .andExpect(jsonPath("$.area", is(hiddenRoomDto.getArea()), Double.class))
                .andExpect(jsonPath("$.number", is(hiddenRoomDto.getNumber())))
                .andExpect(jsonPath("$.categoryDto").value(roomDto.getCategoryDto()))
                .andExpect(jsonPath("$.isVisible", is(hiddenRoomDto.getIsVisible())));

        verify(roomService).hideRoomById(requesterId, roomId);

        when(roomService.hideRoomById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(patch("/rooms/{id}/hide", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(roomService, times(2)).hideRoomById(requesterId, roomId);
    }

    @Test
    @SneakyThrows
    void unhideRoomById() {
        when(roomService.unhideRoomById(anyLong(), eq(roomId))).thenReturn(roomDto);

        mockMvc.perform(patch("/rooms/{id}/unhide", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.area", is(roomDto.getArea()), Double.class))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.categoryDto").value(roomDto.getCategoryDto()))
                .andExpect(jsonPath("$.isVisible", is(roomDto.getIsVisible())));

        verify(roomService).unhideRoomById(requesterId, roomId);

        when(roomService.unhideRoomById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(patch("/rooms/{id}/unhide", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(roomService, times(2)).unhideRoomById(requesterId, roomId);
    }

    @Test
    @SneakyThrows
    void permanentlyDeleteRoomById() {
        mockMvc.perform(delete("/rooms/{Id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNoContent());

        verify(roomService).permanentlyDeleteRoomById(requesterId, roomId);

        doThrow(NotFoundException.class)
                .when(roomService)
                .permanentlyDeleteRoomById(requesterId, roomId);

        mockMvc.perform(delete("/rooms/{Id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNotFound());

        verify(roomService, times(2)).permanentlyDeleteRoomById(requesterId, roomId);
    }

    @Test
    @SneakyThrows
    void getAvailableRoomsByCategoryInDates() {
        when(roomService.getAvailableRoomsByCategoryInDates(anyLong(), anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(roomDto));

        mockMvc.perform(get("/rooms/{catId}/getAvailableRooms", catId)
                        .header(requesterHeader, catId)
                        .accept(MediaType.ALL_VALUE)
                        .param("checkInDate", "01.01.2024" )
                        .param("checkOutDate", "02.01.2024"))
                .andExpect(status().isOk());

        verify(roomService).getAvailableRoomsByCategoryInDates(requesterId, catId, checkIn, checkOut);
        verify(roomService, times(1))
                .getAvailableRoomsByCategoryInDates(requesterId, catId, checkIn, checkOut);
    }
}
