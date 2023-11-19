package ru.dogudacha.PetHotel.room.service;

import ru.dogudacha.PetHotel.room.dto.RoomDto;
import ru.dogudacha.PetHotel.room.dto.UpdateRoomDto;

import java.util.Collection;

public interface RoomService {
    RoomDto addRoom(Long userId, RoomDto roomDto);

    RoomDto getRoomById(Long userId, Long roomId);

    RoomDto updateRoom(Long userId, Long roomId, UpdateRoomDto roomDto);

    Collection<RoomDto> getAllRooms(Long userId);

    void deleteRoomById(Long userId, Long roomId);
}
