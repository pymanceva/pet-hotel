package ru.dogudacha.PetHotel.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.ConflictException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.room.dto.RoomDto;
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
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
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
        checkViewAccess(userId);

        Room room = findRoomById(roomId);
        log.info("RoomService: getRoomById, userId={}, roomId={}", userId, roomId);

        return roomMapper.toRoomDto(room);
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

        if (Objects.isNull(newRoom.getArea())) {
            newRoom.setArea(oldRoom.getArea());
        }

        if (Objects.isNull(newRoom.getNumber())) {
            newRoom.setNumber(oldRoom.getNumber());
        }

        Room updatedRoom = roomRepository.save(newRoom);
        log.info("RoomService: updateRoom, userId={}, roomId={}, roomDto={}", userId, roomId, roomDto);

        return roomMapper.toRoomDto(updatedRoom);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<RoomDto> getAllRooms(Long userId, Boolean isVisible) {
        checkViewAccess(userId);

        List<Room> allRooms = roomRepository.getAllRooms(isVisible).orElse(Collections.emptyList());
        log.info("RoomService: getAllRooms, userId={}, list size={}", userId, allRooms.size());

        return roomMapper.toListRoomDto(allRooms);
    }

    @Transactional
    @Override
    public RoomDto hideRoomById(Long userId, Long roomId) {
        checkAdminAccess(userId);
        Room room = findRoomById(roomId);

        //пока уловие всегда true, в дальнейшем здесь буду проверять наличие активных бронирований у номера
        if (true) {
            room.setIsVisible(false);
            roomRepository.save(room);
            log.info("RoomService: hideRoomById, userId={}, roomId={}", userId, roomId);
            return roomMapper.toRoomDto(room);
        } else {
            throw new ConflictException(String.format("room with id=%d has opened bookings", roomId));
        }
    }

    @Transactional
    @Override
    public RoomDto unhideRoomById(Long userId, Long roomId) {
        checkAdminAccess(userId);
        Room room = findRoomById(roomId);

        room.setIsVisible(true);
        roomRepository.save(room);
        log.info("RoomService: unhideRoomById, userId={}, roomId={}", userId, roomId);
        return roomMapper.toRoomDto(room);
    }

    @Transactional
    @Override
    public void permanentlyDeleteRoomById(Long userId, Long roomId) {
        checkAdminAccess(userId);

        int result = roomRepository.deleteRoomById(roomId);

        if (result == 0) {
            throw new NotFoundException(String.format("room with id=%d not found", roomId));
        }

        log.info("RoomService: permanentlyDeleteRoomById, userId={}, roomId={}", userId, roomId);
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

    private void checkViewAccess(Long userId) {
        User user = findUserById(userId);

        if (user.getRole().ordinal() == 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this information",
                    user.getRole()));
        }
    }
}
