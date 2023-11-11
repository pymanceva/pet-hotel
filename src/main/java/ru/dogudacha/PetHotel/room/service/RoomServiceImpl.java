package ru.dogudacha.PetHotel.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.room.dto.RoomDto;
import ru.dogudacha.PetHotel.room.dto.RoomWithoutPriceDto;
import ru.dogudacha.PetHotel.room.dto.UpdateRoomDto;
import ru.dogudacha.PetHotel.room.dto.mapper.RoomMapper;
import ru.dogudacha.PetHotel.room.model.Room;
import ru.dogudacha.PetHotel.room.repository.RoomRepository;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    final private RoomRepository roomRepository;
    final private RoomMapper roomMapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public RoomDto addRoom(Long userId, RoomDto roomDto) {
        checkAdminAccess(userId);

        Room newRoom = roomMapper.toRoom(roomDto);
        Room addedRoom = roomRepository.save(newRoom);
        log.info("RoomService: addRoom, userId={}, roomDto={}", userId, addedRoom);
        return roomMapper.toRoomDto(addedRoom);
    }

    @Transactional(readOnly = true)
    @Override
    public RoomDto getRoomById(Long userId, Long roomId) {
        checkPriceAccess(userId);

        Room room = findRoomById(roomId);
        log.info("RoomService: getRoomById, userId={}, roomId={}", userId, roomId);

        return roomMapper.toRoomDto(room);
    }

    @Transactional(readOnly = true)
    @Override
    public RoomWithoutPriceDto getRoomWithoutPriceById(Long userId, Long roomId) {
        checkLoginAccess(userId);
        Room room = findRoomById(roomId);
        log.info("RoomService: getRoomWithoutPriceById, userId={}, roomId={}", userId, roomId);

        return roomMapper.toRoomDtoWithoutPrice(room);
    }

    @Transactional
    @Override
    public RoomDto updateRoom(Long userId, Long roomId, UpdateRoomDto roomDto) {
        checkAdminAccess(userId);
        Room oldRoom = findRoomById(roomId);
        Room newRoom = roomMapper.toRoom(roomDto);
        newRoom.setId(roomId);

        if (Objects.isNull(newRoom.getType())) {
            newRoom.setType(oldRoom.getType());
        }

        if (Objects.isNull(newRoom.getSize())) {
            newRoom.setSize(oldRoom.getSize());
        }

        if (Objects.isNull(newRoom.getPrice())) {
            newRoom.setPrice(oldRoom.getPrice());
        }

        if (Objects.isNull(newRoom.getNumber())) {
            newRoom.setNumber(oldRoom.getNumber());
        }

        if (Objects.isNull(newRoom.getIsAvailable())) {
            newRoom.setIsAvailable(oldRoom.getIsAvailable());
        }

        Room updatedRoom = roomRepository.save(newRoom);
        log.info("RoomService: updateRoom, userId={}, roomId={}, roomDto={}", userId, roomId, roomDto);

        return roomMapper.toRoomDto(updatedRoom);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<RoomDto> getAllRooms(Long userId) {
        checkPriceAccess(userId);

        List<Room> allRooms = roomRepository.getAllRooms().orElse(Collections.emptyList());
        log.info("RoomService: getAllRooms, userId={}, list size={}", userId, allRooms.size());

        return roomMapper.toListRoomDto(allRooms);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<RoomWithoutPriceDto> getAllRoomsWithoutPrice(Long userId) {
        checkLoginAccess(userId);
        List<Room> allRooms = roomRepository.getAllRooms().orElse(Collections.emptyList());
        log.info("RoomService: getAllRoomsWithoutPrice, userId={}, list size={}", userId, allRooms.size());

        return roomMapper.toListRoomWithoutPriceDto(allRooms);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long userId, Long roomId) {
        checkAdminAccess(userId);

        int result = roomRepository.deleteRoomById(roomId);

        if (result == 0) {
            throw new NotFoundException(String.format("room with id=%d not found", roomId));
        }

        log.info("RoomService: deleteRoomById, userId={}, roomId={}", userId, roomId);
    }

    private Room findRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("room with id=%d is not found", id)));
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("user with id=%d is not found", userId)));
    }

    private void checkAdminAccess(Long userId) {
        User user = findUserById(userId);

        if (user.getRole().ordinal() >= 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                    user.getRole()));
        }
    }

    private void checkPriceAccess(Long userId) {
        User user = findUserById(userId);

        if (user.getRole().ordinal() == 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this information",
                    user.getRole()));
        }
    }

    private void checkLoginAccess(Long userId) {
        User user = findUserById(userId);
    }
}