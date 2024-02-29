package ru.modgy.booking.service;

import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;

public interface BookingService {
    BookingDto addBooking(Long userId, NewBookingDto newBookingDto);

    BookingDto getBookingById(Long userId, Long bookingId);

    BookingDto updateBooking(Long userId, Long bookingId, UpdateBookingDto updateBookingDto);

    void deleteBookingById(Long userId, Long bookingId);
}
