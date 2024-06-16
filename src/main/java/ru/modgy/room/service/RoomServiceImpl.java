package ru.modgy.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.booking.model.Booking;
import ru.modgy.booking.repository.BookingRepository;
import ru.modgy.exception.ConflictException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.dto.NewRoomDto;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.dto.UpdateRoomDto;
import ru.modgy.room.dto.mapper.RoomMapper;
import ru.modgy.room.model.Room;
import ru.modgy.room.repository.RoomRepository;
import ru.modgy.utility.EntityService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final BookingRepository bookingRepository;
    private final EntityService entityService;

    @Transactional
    @Override
    public RoomDto addRoom(Long userId, NewRoomDto newRoomDto) {
        Room newRoom = roomMapper.toRoom(newRoomDto);
        Category category = entityService.getCategoryIfExists(newRoomDto.getCategoryId());
        newRoom.setCategory(category);
        Room addedRoom = roomRepository.save(newRoom);
        RoomDto addedRoomDto = roomMapper.toRoomDto(addedRoom);
        log.info("RoomService: addRoom, userId={}, roomDto={}", userId, addedRoom);
        return addedRoomDto;
    }

    @Transactional(readOnly = true)
    @Override
    public RoomDto getRoomById(Long userId, Long roomId) {
        Room room = entityService.getRoomIfExists(roomId);
        RoomDto roomDto = roomMapper.toRoomDto(room);
        log.info("RoomService: getRoomById, userId={}, roomId={}", userId, roomId);
        return roomDto;
    }

    @Transactional
    @Override
    public RoomDto updateRoom(Long userId, Long roomId, UpdateRoomDto updateRoomDto) {
        Room oldRoom = entityService.getRoomIfExists(roomId);
        Room newRoom = roomMapper.toRoom(updateRoomDto);
        Category category;
        if (updateRoomDto.getCategoryId() != null) {
            category = entityService.getCategoryIfExists(updateRoomDto.getCategoryId());
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

        log.info("RoomService: updateRoom, userId={}, roomId={}, roomDto={}", userId, roomId, updateRoomDto);

        return updatedRoomDto;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<RoomDto> getAllRooms(Long userId, Boolean isVisible) {
        List<Room> allRooms = roomRepository.getAllRooms(isVisible).orElse(Collections.emptyList());
        return roomMapper.toListRoomDto(allRooms);
    }

    @Transactional
    @Override
    public RoomDto hideRoomById(Long userId, Long roomId) {
        Room room = entityService.getRoomIfExists(roomId);

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
        Room room = entityService.getRoomIfExists(roomId);

        room.setIsVisible(true);
        roomRepository.save(room);
        log.info("RoomService: unhideRoomById, userId={}, roomId={}", userId, roomId);
        return roomMapper.toRoomDto(room);
    }

    @Transactional
    @Override
    public void permanentlyDeleteRoomById(Long userId, Long roomId) {
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
        List<Room> foundRooms = findAvailableRoomsByCategoryInDates(catId, checkInDate, checkOutDate);
        log.info("RoomService: getAvailableRoomsByCategoryInDates, " +
                "userId={}, catId={}, checkInDate={}, checkOutDate={}",
                userId, catId, checkInDate, checkOutDate);
        return roomMapper.toListRoomDto(foundRooms);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkUniqueRoomNumber(Long userId, String roomNumber) {
        boolean isUnique = false;
        int  result = roomRepository.countAllByNumber(roomNumber);

        if (result == 0) {
            isUnique = true;
        }

        log.info("RoomService: checkUniqueRoomNumber, userId={}, roomNumber={}, result={}", userId, roomNumber, isUnique);
        return isUnique;
    }

    private List<Room> findAvailableRoomsByCategoryInDates(Long catId,
                                                           LocalDate checkInDate,
                                                           LocalDate checkOutDate) {
        return roomRepository.findAvailableRoomsByCategoryInDates(catId, checkInDate, checkOutDate)
                .orElse(Collections.emptyList());
    }
}
