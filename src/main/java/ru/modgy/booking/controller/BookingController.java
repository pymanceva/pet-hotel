package ru.modgy.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.service.BookingService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String USER_ID = "X-PetHotel-User-Id";
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBooking(@RequestHeader(USER_ID) Long requesterId,
                                 @RequestBody @Valid NewBookingDto newBookingDto) {
        log.info("BookingController: POST/addBooking, requesterId={}, booking={}", requesterId, newBookingDto);
        return bookingService.addBooking(requesterId, newBookingDto);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@RequestHeader(USER_ID) Long requesterId,
                                     @PathVariable("id") Long bookingId) {
        log.info("BookingController: GET/getBookingById, requesterId={}, bookingId={}", requesterId, bookingId);
        return bookingService.getBookingById(requesterId, bookingId);
    }

    @PatchMapping("/{id}")
    public BookingDto updateBooking(@RequestHeader(USER_ID) Long requesterId,
                                    @RequestBody @Valid UpdateBookingDto updateBookingDto,
                                    @PathVariable("id") Long bookingId) {
        log.info("BookingController: PATCH/updateBooking, requesterId={}, bookingId={}, requestBody={}",
                requesterId, bookingId, updateBookingDto);
        return bookingService.updateBooking(requesterId, bookingId, updateBookingDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingById(@RequestHeader(USER_ID) Long requesterId,
                                  @PathVariable("id") Long bookingId) {
        log.info("BookingController: DELETE/deleteBookingById, requesterId={}, bookingId={}", requesterId, bookingId);
        bookingService.deleteBookingById(requesterId, bookingId);
    }

    @GetMapping("/{roomId}/bookingsOfRoomInDates")
    public List<BookingDto> findCrossingBookingsForRoomInDates(@RequestHeader(USER_ID) Long requesterId,
                                                       @PathVariable("roomId") Long roomId,
                                                       @Param("checkInDate") LocalDate checkInDate,
                                                       @Param("checkOutDate") LocalDate checkOutDate) {
        log.info("BookingController: GET/findBookingsForRoomInDates, requesterId={}, roomId={}", requesterId, roomId);
        return bookingService.findCrossingBookingsForRoomInDates(requesterId, roomId, checkInDate, checkOutDate);
    }

    @GetMapping("/{roomId}/checkRoomAvailable")
    public void checkRoomAvailableInDates(@RequestHeader(USER_ID) Long requesterId,
                                          @PathVariable("roomId") Long roomId,
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate) {
        log.info("BookingController: GET/checkRoomAvailableInDates, requesterId={}, roomId={}", requesterId, roomId);
        bookingService.checkRoomAvailableInDates(requesterId, roomId, checkInDate, checkOutDate);
    }
}
