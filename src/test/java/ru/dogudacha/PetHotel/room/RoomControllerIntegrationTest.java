package ru.dogudacha.PetHotel.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.room.RoomController;
import ru.dogudacha.PetHotel.room.dto.RoomDto;
import ru.dogudacha.PetHotel.room.dto.RoomWithoutPriceDto;
import ru.dogudacha.PetHotel.room.dto.UpdateRoomDto;
import ru.dogudacha.PetHotel.room.model.RoomTypes;
import ru.dogudacha.PetHotel.room.service.RoomService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoomController.class)
public class RoomControllerIntegrationTest {
    private final String requesterHeader = "X-PetHotel-User-Id";
    long requesterId = 1L;
    long roomId = 1L;
    private final RoomDto roomDto = RoomDto.builder()
            .id(roomId)
            .size(5.0)
            .price(1000.0)
            .number("standard room")
            .type(RoomTypes.STANDARD)
            .isAvailable(true)
            .build();
    private final RoomWithoutPriceDto roomWithoutPriceDto = RoomWithoutPriceDto.builder()
            .id(roomId)
            .size(5.0)
            .number("standard room")
            .type(RoomTypes.STANDARD)
            .isAvailable(true)
            .build();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoomService roomService;

    @Test
    @SneakyThrows
    void addRoom() {
        when(roomService.addRoom(anyLong(), any(RoomDto.class))).thenReturn(roomDto);

        mockMvc.perform(post("/rooms")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.size", is(roomDto.getSize()), Double.class))
                .andExpect(jsonPath("$.price", is(roomDto.getPrice()), Double.class))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.type", is(roomDto.getType().toString())))
                .andExpect(jsonPath("$.isAvailable", is(roomDto.getIsAvailable())));

        verify(roomService).addRoom(anyLong(), any(RoomDto.class));

        mockMvc.perform(post("/rooms")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new RoomDto())))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/rooms")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new RoomDto())))
                .andExpect(status().isBadRequest());

        verify(roomService, times(1)).addRoom(anyLong(), any(RoomDto.class));
    }

    @Test
    @SneakyThrows
    void getRoomWithoutPriceById() {
        when(roomService.getRoomWithoutPriceById(anyLong(), anyLong())).thenReturn(roomWithoutPriceDto);

        mockMvc.perform(get("/rooms/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.size", is(roomDto.getSize()), Double.class))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.type", is(roomDto.getType().toString())))
                .andExpect(jsonPath("$.isAvailable", is(roomDto.getIsAvailable())));

        verify(roomService).getRoomWithoutPriceById(requesterId, roomId);

        when(roomService.getRoomWithoutPriceById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/rooms/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(roomService, times(2)).getRoomWithoutPriceById(requesterId, roomId);
    }

    @Test
    @SneakyThrows
    void getRoomById() {
        when(roomService.getRoomById(anyLong(), anyLong())).thenReturn(roomDto);

        mockMvc.perform(get("/rooms/{id}/withPrice", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.size", is(roomDto.getSize()), Double.class))
                .andExpect(jsonPath("$.price", is(roomDto.getPrice()), Double.class))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.type", is(roomDto.getType().toString())))
                .andExpect(jsonPath("$.isAvailable", is(roomDto.getIsAvailable())));

        verify(roomService).getRoomById(requesterId, roomId);

        when(roomService.getRoomById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/rooms/{id}/withPrice", roomId)
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
                        .content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.size", is(roomDto.getSize()), Double.class))
                .andExpect(jsonPath("$.price", is(roomDto.getPrice()), Double.class))
                .andExpect(jsonPath("$.number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.type", is(roomDto.getType().toString())))
                .andExpect(jsonPath("$.isAvailable", is(roomDto.getIsAvailable())));


        when(roomService.updateRoom(anyLong(), eq(roomId), any(UpdateRoomDto.class)))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/rooms/{id}", roomId)
                        .header(requesterHeader, requesterId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(roomDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void getAllRooms() {
        when(roomService.getAllRooms(anyLong())).thenReturn(List.of(roomDto));

        mockMvc.perform(get("/rooms/withPrice")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].size", is(roomDto.getSize()), Double.class))
                .andExpect(jsonPath("$.[0].price", is(roomDto.getPrice()), Double.class))
                .andExpect(jsonPath("$.[0].number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.[0].type", is(roomDto.getType().toString())))
                .andExpect(jsonPath("$.[0].isAvailable", is(roomDto.getIsAvailable())));
    }

    @Test
    @SneakyThrows
    void getAllRoomsWithoutPrice() {
        when(roomService.getAllRoomsWithoutPrice(anyLong())).thenReturn(List.of(roomWithoutPriceDto));

        mockMvc.perform(get("/rooms")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(roomDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].size", is(roomDto.getSize()), Double.class))
                .andExpect(jsonPath("$.[0].number", is(roomDto.getNumber())))
                .andExpect(jsonPath("$.[0].type", is(roomDto.getType().toString())))
                .andExpect(jsonPath("$.[0].isAvailable", is(roomDto.getIsAvailable())));
    }

    @Test
    @SneakyThrows
    void deleteRoomById() {
        mockMvc.perform(delete("/rooms/{Id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNoContent());

        verify(roomService).deleteRoomById(requesterId, roomId);

        doThrow(NotFoundException.class)
                .when(roomService)
                .deleteRoomById(requesterId, roomId);

        mockMvc.perform(delete("/rooms/{Id}", roomId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNotFound());

        verify(roomService, times(2)).deleteRoomById(requesterId, roomId);
    }
}