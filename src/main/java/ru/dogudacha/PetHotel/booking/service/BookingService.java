package ru.dogudacha.PetHotel.booking.service;

import ru.dogudacha.PetHotel.booking.dto.BookingDto;
import ru.dogudacha.PetHotel.booking.dto.NewBookingDto;
import ru.dogudacha.PetHotel.booking.dto.UpdateBookingDto;

public interface BookingService {
    BookingDto addBooking(Long userId, NewBookingDto newBookingDto);

    BookingDto getBookingById(Long userId, Long bookingId);

    BookingDto updateBooking(Long userId, Long bookingId, UpdateBookingDto updateBookingDto);

    void deleteBookingById(Long userId, Long bookingId);
}
