package ru.modgy.room.service;

import ru.modgy.room.dto.NewRoomDto;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.dto.UpdateRoomDto;

import java.util.Collection;

public interface RoomService {
    RoomDto addRoom(Long userId, NewRoomDto newRoomDto);

    RoomDto getRoomById(Long userId, Long roomId);

    RoomDto updateRoom(Long userId, Long roomId, UpdateRoomDto roomDto);

    Collection<RoomDto> getAllRooms(Long userId, Boolean isVisible);

    RoomDto hideRoomById(Long userId, Long roomId);

    RoomDto unhideRoomById(Long userId, Long roomId);

    void permanentlyDeleteRoomById(Long userId, Long roomId);
}
