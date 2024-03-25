package ru.modgy.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.booking.model.Booking;
import ru.modgy.booking.repository.BookingRepository;
import ru.modgy.exception.AccessDeniedException;
import ru.modgy.exception.ConflictException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.room.category.dto.mapper.CategoryMapper;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.category.repository.CategoryRepository;
import ru.modgy.room.dto.NewRoomDto;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.dto.UpdateRoomDto;
import ru.modgy.room.dto.mapper.RoomMapper;
import ru.modgy.room.model.Room;
import ru.modgy.room.repository.RoomRepository;
import ru.modgy.user.model.User;
import ru.modgy.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    final private RoomRepository roomRepository;
    final private RoomMapper roomMapper;
    final private CategoryRepository categoryRepository;
    final private UserRepository userRepository;
    final private BookingRepository bookingRepository;

    @Transactional
    @Override
    public RoomDto addRoom(Long userId, NewRoomDto newRoomDto) {
        checkAdminAccess(userId);

        Room newRoom = roomMapper.toRoom(newRoomDto);
        Category category = findCategoryById(newRoomDto.getCategoryId());
        newRoom.setCategory(category);
        Room addedRoom = roomRepository.save(newRoom);
        RoomDto addedRoomDto = roomMapper.toRoomDto(addedRoom);
        log.info("RoomService: addRoom, userId={}, roomDto={}", userId, addedRoom);
        return addedRoomDto;
    }

    @Transactional(readOnly = true)
    @Override
    public RoomDto getRoomById(Long userId, Long roomId) {
        checkViewAccess(userId);

        Room room = findRoomById(roomId);
        RoomDto roomDto = roomMapper.toRoomDto(room);
        log.info("RoomService: getRoomById, userId={}, roomId={}", userId, roomId);
        return roomDto;
    }

    @Transactional
    @Override
    public RoomDto updateRoom(Long userId, Long roomId, UpdateRoomDto roomDto) {
        checkAdminAccess(userId);
        Room oldRoom = findRoomById(roomId);
        Room newRoom = roomMapper.toRoom(roomDto);
        Category category;
        if (roomDto.getCategoryId() != null) {
            category = findCategoryById(roomDto.getCategoryId());
        } else {
            category = oldRoom.getCategory();
        }
        newRoom.setId(roomId);
        newRoom.setCategory(category);
        newRoom.setIsVisible(oldRoom.getIsVisible());

        if (Objects.isNull(newRoom.getCategory())) {
            newRoom.setCategory(oldRoom.getCategory());
        }

        if (Objects.isNull(newRoom.getArea())) {
            newRoom.setArea(oldRoom.getArea());
        }

        if (Objects.isNull(newRoom.getNumber())) {
            newRoom.setNumber(oldRoom.getNumber());
        }

        Room updatedRoom = roomRepository.save(newRoom);
        RoomDto updatedRoomDto = roomMapper.toRoomDto(updatedRoom);

        log.info("RoomService: updateRoom, userId={}, roomId={}, roomDto={}", userId, roomId, roomDto);

        return updatedRoomDto;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<RoomDto> getAllRooms(Long userId, Boolean isVisible) {
        checkViewAccess(userId);

        List<Room> allRooms = roomRepository.getAllRooms(isVisible).orElse(Collections.emptyList());
        List<RoomDto> allRoomsDto = new ArrayList<>();
        for (Room room : allRooms) {
            RoomDto roomDto = roomMapper.toRoomDto(room);
            allRoomsDto.add(roomDto);
        }
        log.info("RoomService: getAllRooms, userId={}, list size={}", userId, allRooms.size());

        return allRoomsDto;
    }

    @Transactional
    @Override
    public RoomDto hideRoomById(Long userId, Long roomId) {
        checkAdminAccess(userId);
        Room room = findRoomById(roomId);

        List<Booking> futureBookings = bookingRepository.findFutureBookingsForRoom(roomId, LocalDate.now())
                .orElse(Collections.emptyList());
        if (futureBookings.isEmpty()) {
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

    @Transactional(readOnly = true)
    @Override
    public List<RoomDto> getAvailableRoomsByCategoryInDates(
            Long userId,
            Long catId,
            LocalDate checkInDate,
            LocalDate checkOutDate) {
        checkAdminAccess(userId);
        List<Room> foundRooms = findAvailableRoomsByCategoryInDates(catId, checkInDate, checkOutDate);
        log.info("RoomService: findAvailableRoomsByCategoryInDates, " +
                "userId={}, catId={}, checkInDate={}, checkOutDate={}",
                userId, catId, checkInDate, checkOutDate);
        return roomMapper.toListRoomDto(foundRooms);
    }

    private List<Room> findAvailableRoomsByCategoryInDates(Long catId,
                                                           LocalDate checkInDate,
                                                           LocalDate checkOutDate) {
        return roomRepository.findAvailableRoomsByCategoryInDates(catId, checkInDate, checkOutDate)
                .orElse(Collections.emptyList());
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

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("category with id=%d is not found", id)));
    }
}
