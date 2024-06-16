package ru.modgy.room.service;

import ru.modgy.room.dto.NewRoomDto;
import ru.modgy.room.dto.RoomDto;
import ru.modgy.room.dto.UpdateRoomDto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface RoomService {
    /**
     * Добавление нового номера
     *
     * @param userId     - id пользователя, направляющего запрос
     * @param newRoomDto - данные добавляемого номера
     * @return данные добавленного номера
     */
    RoomDto addRoom(Long userId, NewRoomDto newRoomDto);

    /**
     * Получение по id информации о номере
     *
     * @param userId - id пользователя, направляющего запрос
     * @param roomId - id запрашиваемого номера
     * @return данные запрашиваемого номера
     */
    RoomDto getRoomById(Long userId, Long roomId);

    /**
     * Обновление информации о номере
     *
     * @param userId        - id пользователя, направляющего запрос
     * @param roomId        - id обновляемого номера
     * @param updateRoomDto - обновляемые данные
     * @return данные обновленного номера
     */
    RoomDto updateRoom(Long userId, Long roomId, UpdateRoomDto updateRoomDto);

    /**
     * Получение списка всех номеров
     *
     * @param userId    - id пользователя, направляющего запрос
     * @param isVisible - видимость номера (отражается ли он в списках для пользователей ПО).
     * @return список номеров
     */
    Collection<RoomDto> getAllRooms(Long userId, Boolean isVisible);

    /**
     * Изменение видимости (статуса отображения) номера на false, т.е. сокрытие номера
     *
     * @param userId - id пользователя, направляющего запрос
     * @param roomId - id скрываемого номера
     * @return скрытый номер
     */
    RoomDto hideRoomById(Long userId, Long roomId);

    /**
     * Изменение видимости (статуса отображения) номера на true, т.е. отображение номера
     *
     * @param userId - id пользователя, направляющего запрос
     * @param roomId - id отображаемого номера
     * @return отображаемый номер
     */
    RoomDto unhideRoomById(Long userId, Long roomId);

    /**
     * Удаление по id информации о номере
     *
     * @param userId    - id пользователя, направляющего запрос
     * @param roomId - id удаляемого номера
     */
    void permanentlyDeleteRoomById(Long userId, Long roomId);

    /**
     * Получение списка всех номеров заданной категории, не имеющих активных бронирований в указанные даты
     *
     * @param userId    - id пользователя, направляющего запрос
     * @param catId - id категории номеров
     * @param checkInDate - дата заезда, с которой начинается временной интервал для проверки наличия бронирований
     * @param checkOutDate - дата выезда, на которой заканчивается временной интервал для проверки наличия бронирований
     * @return список найденных номеров
     */
    List<RoomDto> getAvailableRoomsByCategoryInDates(Long userId, Long catId, LocalDate checkInDate, LocalDate checkOutDate);

    /**
     * Проверка номера комнаты на уникальность.
     *
     * @param userId - id пользователя, направляющего запрос
     * @param roomNumber - номер, который проверяется на уникальность в БД
     * @return true - если такой номер уникален,
     * false - если номер не уникален и в БД имеется запись о комнате с таким же номером.
     */
    boolean checkUniqueRoomNumber(Long userId, String roomNumber);
}
