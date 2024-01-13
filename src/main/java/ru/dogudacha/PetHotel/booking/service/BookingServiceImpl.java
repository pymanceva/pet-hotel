package ru.dogudacha.PetHotel.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.booking.dto.BookingDto;
import ru.dogudacha.PetHotel.booking.dto.NewBookingDto;
import ru.dogudacha.PetHotel.booking.dto.UpdateBookingDto;
import ru.dogudacha.PetHotel.booking.dto.mapper.BookingMapper;
import ru.dogudacha.PetHotel.booking.model.Booking;
import ru.dogudacha.PetHotel.booking.model.StatusBooking;
import ru.dogudacha.PetHotel.booking.model.TypesBooking;
import ru.dogudacha.PetHotel.booking.repository.BookingRepository;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.repository.PetRepository;
import ru.dogudacha.PetHotel.room.model.Room;
import ru.dogudacha.PetHotel.room.repository.RoomRepository;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    final private BookingRepository bookingRepository;
    final private BookingMapper bookingMapper;
    final private RoomRepository roomRepository;
    final private PetRepository petRepository;
    final private UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto addBooking(Long userId, NewBookingDto newBookingDto) {
        checkAdminAccess(userId);

        Booking newBooking = bookingMapper.toBooking(newBookingDto);

        Room room = findRoomById(newBookingDto.getRoomId());
        newBooking.setRoom(room);

        Set<Pet> pets = newBookingDto.getPetIds().stream()
                .map(this::findPetById)
                .collect(Collectors.toSet());
        newBooking.setPets(pets);

        if (newBooking.getStatus() == null) {
            if (newBooking.getIsPrepaid() | newBooking.getType().equals(TypesBooking.TYPE_CLOSING)) {
                newBooking.setStatus(StatusBooking.STATUS_CONFIRMED);
            } else {
                newBooking.setStatus(StatusBooking.STATUS_INITIAL);
            }
        }

        Booking addedBooking = bookingRepository.save(newBooking);

        log.info("BookingService: addBooking, userId={}, bookingDto={}", userId, addedBooking);

        return bookingMapper.toBookingDto(addedBooking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        checkAdminAccess(userId);

        Booking booking = findBookingById(bookingId);
        log.info("BookingService: getBookingById, userId={}, bookingId={}", userId, bookingId);
        return bookingMapper.toBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto updateBooking(Long userId, Long bookingId, UpdateBookingDto updateBookingDto) {
        checkAdminAccess(userId);

        Booking oldBooking = findBookingById(bookingId);
        Booking newBooking = bookingMapper.toBooking(updateBookingDto);
        newBooking.setId(oldBooking.getId());
        newBooking.setType(oldBooking.getType());

        if (Objects.isNull(newBooking.getCheckInDate())) {
            newBooking.setCheckInDate(oldBooking.getCheckInDate());
        }

        if (Objects.isNull(newBooking.getCheckOutDate())) {
            newBooking.setCheckOutDate(oldBooking.getCheckOutDate());
        }

        if (Objects.isNull(newBooking.getCheckInTime())) {
            newBooking.setCheckInTime(oldBooking.getCheckInTime());
        }

        if (Objects.isNull(newBooking.getCheckOutTime())) {
            newBooking.setCheckOutTime(oldBooking.getCheckOutTime());
        }

        if (Objects.isNull(newBooking.getStatus())) {
            newBooking.setStatus(oldBooking.getStatus());
        }

        if (Objects.isNull(newBooking.getReasonOfStop())) {
            newBooking.setReasonOfStop(oldBooking.getReasonOfStop());
        }

        if (Objects.isNull(newBooking.getReasonOfCancel())) {
            newBooking.setReasonOfCancel(oldBooking.getReasonOfCancel());
        }

        if (Objects.isNull(newBooking.getPrice())) {
            newBooking.setPrice(oldBooking.getPrice());
        }

        if (Objects.isNull(newBooking.getAmount())) {
            newBooking.setAmount(oldBooking.getAmount());
        }

        if (Objects.isNull(newBooking.getPrepaymentAmount())) {
            newBooking.setPrepaymentAmount(oldBooking.getPrepaymentAmount());
        }

        if (Objects.isNull(newBooking.getIsPrepaid())) {
            newBooking.setIsPrepaid(oldBooking.getIsPrepaid());
        }

        if (Objects.isNull(newBooking.getComment())) {
            newBooking.setComment(oldBooking.getComment());
        }

        if (Objects.isNull(newBooking.getFileUrl())) {
            newBooking.setFileUrl(oldBooking.getFileUrl());
        }

        if (updateBookingDto.getRoomId() != null) {
            newBooking.setRoom(findRoomById(updateBookingDto.getRoomId()));
        } else {
            newBooking.setRoom(oldBooking.getRoom());
        }

        if (updateBookingDto.getPetIds() != null) {
            Set<Pet> pets = updateBookingDto.getPetIds().stream()
                    .map(this::findPetById)
                    .collect(Collectors.toSet());
            newBooking.setPets(pets);
        } else {
            newBooking.setPets(oldBooking.getPets());
        }

        if (newBooking.getStatus().equals(StatusBooking.STATUS_INITIAL) & newBooking.getIsPrepaid()) {
            newBooking.setStatus(StatusBooking.STATUS_CONFIRMED);
        }

        Booking updatedBooking = bookingRepository.save(newBooking);
        BookingDto updatedBookingDto = bookingMapper.toBookingDto(updatedBooking);
        log.info("BookingService: updateBooking, userId={}, bookingId={}, updateBookingDto={}",
                userId, bookingId, updateBookingDto);

        return updatedBookingDto;
    }

    @Transactional
    @Override
    public void deleteBookingById(Long userId, Long bookingId) {
        checkAdminAccess(userId);

        int result = bookingRepository.deleteBookingById(bookingId);

        if (result == 0) {
            throw new NotFoundException(String.format("booking with id=%d not found", bookingId));
        }

        log.info("BookingService: deleteBookingById, userId={}, bookingId={}", userId, bookingId);
    }

    private Booking findBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("booking with id=%d is not found", id)));
    }

    private Pet findPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("pet with id=%d is not found", id)));
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
}
