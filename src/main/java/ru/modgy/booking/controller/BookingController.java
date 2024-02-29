package ru.modgy.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.service.BookingService;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBooking(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                 @RequestBody @Valid NewBookingDto newBookingDto) {
        log.info("BookingController: POST/addBooking, requesterId={}, booking={}", requesterId, newBookingDto);
        return bookingService.addBooking(requesterId, newBookingDto);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                     @PathVariable("id") Long bookingId) {
        log.info("BookingController: GET/getBookingById, requesterId={}, bookingId={}", requesterId, bookingId);
        return bookingService.getBookingById(requesterId, bookingId);
    }

    @PatchMapping("/{id}")
    public BookingDto updateBooking(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                    @RequestBody @Valid UpdateBookingDto updateBookingDto,
                                    @PathVariable("id") Long bookingId) {
        log.info("BookingController: PATCH/updateBooking, requesterId={}, bookingId={}, requestBody={}",
                requesterId, bookingId, updateBookingDto);
        return bookingService.updateBooking(requesterId, bookingId, updateBookingDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                  @PathVariable("id") Long bookingId) {
        log.info("BookingController: DELETE/deleteBookingById, requesterId={}, bookingId={}", requesterId, bookingId);
        bookingService.deleteBookingById(requesterId, bookingId);
    }
}
