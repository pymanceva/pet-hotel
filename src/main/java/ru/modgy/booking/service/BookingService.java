package ru.modgy.booking.service;

import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingDto addBooking(Long userId, NewBookingDto newBookingDto);

    BookingDto getBookingById(Long userId, Long bookingId);

    BookingDto updateBooking(Long userId, Long bookingId, UpdateBookingDto updateBookingDto);

    void deleteBookingById(Long userId, Long bookingId);

    List<BookingDto> findCrossingBookingsForRoomInDates(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    void checkRoomAvailableInDates(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}
