package ru.modgy.booking.service;

import ru.modgy.booking.dto.BookingDto;
import ru.modgy.booking.dto.NewBookingDto;
import ru.modgy.booking.dto.UpdateBookingDto;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    /**
     * Добавление нового бронирования (типа бронирование или типа закрытие)
     *
     * @param userId        - id пользователя, направляющего запрос
     * @param newBookingDto - данные добавляемого бронирования
     * @return данные добавленного бронирования
     */
    BookingDto addBooking(Long userId, NewBookingDto newBookingDto);

    /**
     * Получение по id информации о бронировании
     *
     * @param userId    - id пользователя, направляющего запрос
     * @param bookingId - id запрашиваемого бронирования
     * @return данные запрашиваемого бронирования
     */
    BookingDto getBookingById(Long userId, Long bookingId);

    /**
     * Обновление информации о бронировании
     *
     * @param userId           - id пользователя, направляющего запрос
     * @param bookingId        - id обновляемого бронирования
     * @param updateBookingDto - обновляемые данные
     * @return данные обновленного бронирования
     */
    BookingDto updateBooking(Long userId, Long bookingId, UpdateBookingDto updateBookingDto);

    /**
     * Удаление по id информации о бронировании
     *
     * @param userId    - id пользователя, направляющего запрос
     * @param bookingId - id удаляемого бронирования
     */
    void deleteBookingById(Long userId, Long bookingId);

    /**
     * Поиск пересекающихся бронирований. Поиск бронирований, пересекающихся с выбранными датами.
     * Например, дата окончания имеющегося в БД бронирования = checkInDate в текущем запросе
     * или дата начала имеющегося в БД бронирования = checkOutDate в текущем запросе.
     *
     * @param userId       - id пользователя, направляющего запрос
     * @param roomId       - id номера, бронирования которого проверяются
     * @param checkInDate  - дата заезда, по которой ведется поиск пересечений
     * @param checkOutDate - дата выезда, по которой ведется поиск пересечений
     * @return список пересекающихся бронирований
     */
    List<BookingDto> findCrossingBookingsForRoomInDates(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    /**
     * Проверка доступности номера для бронирования в выбранные даты
     *
     * @param userId       - id пользователя, направляющего запрос
     * @param roomId       - id номера, бронирования которого проверяются
     * @param checkInDate  - дата заезда, с которой начинается временной интервал для проверки доступности номера
     * @param checkOutDate - дата выезда, на которой заканчивается временной интервал для проверки доступности номера
     */
    void checkRoomAvailableInDates(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    /**
     * Поиск блокирующих бронирований, накладывающихся на выбранные даты, которые не позволяют добавить новое бронирование
     *
     * @param userId       - id пользователя, направляющего запрос
     * @param roomId       - id номера, бронирования которого проверяются
     * @param checkInDate  - дата заезда, с которой начинается временной интервал для проверки наличия блокирующих бронирований
     * @param checkOutDate - дата выезда, на которой заканчивается временной интервал для проверки наличия блокирующих бронирований
     * @return список блокирующих бронирований
     */
    List<BookingDto> findBlockingBookingsForRoomInDates(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate);
}
