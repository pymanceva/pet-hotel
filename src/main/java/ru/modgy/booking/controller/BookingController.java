package ru.modgy.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;
import ru.modgy.booking.service.BookingService;
import ru.modgy.utility.UtilityService;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UtilityService utilityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBooking(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                 @RequestBody @Valid NewBookingDto newBookingDto) {
        utilityService.checkBossAdminAccess(requesterId);
        log.info("BookingController: POST/addBooking, requesterId={}, booking={}", requesterId, newBookingDto);
        utilityService.checkBossAdminAccess(requesterId);
        return bookingService.addBooking(requesterId, newBookingDto);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                     @PathVariable("id") Long bookingId) {
        utilityService.checkBossAdminAccess(requesterId);
        log.info("BookingController: GET/getBookingById, requesterId={}, bookingId={}", requesterId, bookingId);
        utilityService.checkBossAdminAccess(requesterId);
        return bookingService.getBookingById(requesterId, bookingId);
    }

    @PatchMapping("/{id}")
    public BookingDto updateBooking(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                    @RequestBody @Valid UpdateBookingDto updateBookingDto,
                                    @PathVariable("id") Long bookingId) {
        utilityService.checkBossAdminAccess(requesterId);
        log.info("BookingController: PATCH/updateBooking, requesterId={}, bookingId={}, requestBody={}",
                requesterId, bookingId, updateBookingDto);
        utilityService.checkBossAdminAccess(requesterId);
        return bookingService.updateBooking(requesterId, bookingId, updateBookingDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                  @PathVariable("id") Long bookingId) {
        utilityService.checkBossAdminAccess(requesterId);
        log.info("BookingController: DELETE/deleteBookingById, requesterId={}, bookingId={}", requesterId, bookingId);
        utilityService.checkBossAdminAccess(requesterId);
        bookingService.deleteBookingById(requesterId, bookingId);
    }

    @GetMapping("/rooms/{roomId}/crossingBookingsOfRoomInDates")
    public List<BookingDto> findCrossingBookingsForRoomInDates(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                                       @PathVariable("roomId") Long roomId,
                                                       @RequestParam("checkInDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate checkInDate,
                                                       @RequestParam("checkOutDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate checkOutDate) {
        log.info("BookingController: GET/findBookingsForRoomInDates, requesterId={}, roomId={}", requesterId, roomId);
        utilityService.checkBossAdminAccess(requesterId);
        return bookingService.findCrossingBookingsForRoomInDates(requesterId, roomId, checkInDate, checkOutDate);
    }

    @GetMapping("/rooms/{roomId}/checkRoomAvailable")
    public void checkRoomAvailableInDates(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                          @PathVariable("roomId") Long roomId,
                                          @RequestParam("checkInDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate checkInDate,
                                          @RequestParam("checkOutDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate checkOutDate) {
        log.info("BookingController: GET/checkRoomAvailableInDates, requesterId={}, roomId={}", requesterId, roomId);
        utilityService.checkBossAdminAccess(requesterId);
        bookingService.checkRoomAvailableInDates(requesterId, roomId, checkInDate, checkOutDate);
    }

    @GetMapping("/{bookingId}/rooms/{roomId}/checkUpdateRoomAvailable")
    public void checkUpdateBookingRoomAvailableInDates(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                                       @PathVariable("roomId") Long roomId,
                                                       @PathVariable("bookingId") Long bookingId,
                                                       @RequestParam("checkInDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate checkInDate,
                                                       @RequestParam("checkOutDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate checkOutDate) {
        log.info("BookingController: GET/checkUpdateRoomAvailableInDates, requesterId={}, roomId={}, bookingId={}", requesterId, roomId, bookingId);
        utilityService.checkBossAdminAccess(requesterId);
        bookingService.checkUpdateBookingRoomAvailableInDates(requesterId, roomId, bookingId, checkInDate, checkOutDate);
    }

    @GetMapping("/rooms/{roomId}/blockingBookingsInDates")
    public List<BookingDto> findBlockingBookingsForRoomInDates(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                                               @PathVariable("roomId") Long roomId,
                                                               @RequestParam("checkInDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate checkInDate,
                                                               @RequestParam("checkOutDate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate checkOutDate) {
        log.info("BookingController: GET/findBlockingBookingsForRoomInDates, requesterId={}, roomId={}", requesterId, roomId);
        utilityService.checkBossAdminAccess(requesterId);
        return bookingService.findBlockingBookingsForRoomInDates(requesterId, roomId, checkInDate, checkOutDate);
    }
}
