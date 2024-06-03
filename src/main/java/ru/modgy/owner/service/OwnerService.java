package ru.modgy.owner.service;

import ru.modgy.owner.controller.SearchDirection;
import ru.modgy.owner.dto.*;

import java.util.Collection;
import java.util.List;

public interface OwnerService {
    /**
     * Добавление нового клиента (хозяина питомца)
     *
     * @param requesterId - id пользователя, направляющего запрос
     * @param newOwnerDto - данные добавляемого клиента (хозяина питомца)
     * @return данные добавленного клиента (хозяина питомца)
     */
    OwnerDto addOwner(Long requesterId, NewOwnerDto newOwnerDto);

    /**
     * Получение по id краткой информации о клиенте (хозяине питомца)
     *
     * @param requesterId - id пользователя, направляющего запрос
     * @param id          - id запрашиваемого клиента (хозяина питомца)
     * @return краткие данные клиента (хозяина питомца)
     */
    OwnerShortDto getShortOwnerById(Long requesterId, Long id);

    /**
     * Получение краткой информации о последних num зарегистрированных клиентах (хозяевах питомцев)
     *
     * @param requesterId - id пользователя, направляющего запрос
     * @param num         - число запрашиваемых клиентов
     * @return список краткой иноформации о num последних зарегистрированных клиентов
     */
    List<OwnerShortDto> getSomeShortOwners(Long requesterId, int num);

    /**
     * Получение по id информации о клиенте (хозяине питомца)
     *
     * @param requesterId - id пользователя, направляющего запрос
     * @param ownerId     - id запрашиваемого клиента (хозяина питомца)
     * @return данные клиента (хозяина питомца)
     */
    OwnerDto getOwnerById(Long requesterId, Long ownerId);

    /**
     * Обновление информации о клиенте (хозяине питомца)
     *
     * @param requesterId    - id пользователя, направляющего запрос
     * @param ownerId        - id запрашиваемого клиента (хозяина питомца)
     * @param updateOwnerDto - обновляемые данные
     * @return данные клиента (хозяина питомца) после обновления
     */
    OwnerDto updateOwner(Long requesterId, Long ownerId, UpdateOwnerDto updateOwnerDto);

    /**
     * Получение списка всех клиентов (хозяев питомцев)
     *
     * @param requesterId - id пользователя, направляющего запрос
     * @return список всех клиентов (хозяев питомцев)
     */
    Collection<OwnerDto> getAllOwners(Long requesterId);

    /**
     * Удаление по id информации о клиенте (хозяине питомца)
     *
     * @param requesterId - id пользователя, направляющего запрос
     * @param ownerId     - id удаляемого клиента (хозяина питомца)
     */
    void deleteOwnerById(Long requesterId, Long ownerId);

    /**
     * Проверка номера телефона клиента (хозяина питомца) на уникальность.
     *
     * @param requesterId   - id пользователя, направляющего запрос
     * @param checkOwnerDto - данные для проверки
     * @return клиент, с существующим номером телефона (либо пустой dto в случае, если такой клиент не найден)
     */
    OwnerDto checkOwnerPhoneNumber(Long requesterId, CheckOwnerDto checkOwnerDto);

    /**
     * Поиск клиента(хозяина питомца) по ФИО или номеру телефона
     *
     * @param requesterId    - id пользователя, направляющего запрос
     * @param searchOwnerDto - искомые данные
     * @param searchDirection - направление поиска: Direction.name - ФИО, Direction.phone - номер телефона
     * @return список найденных клиентов(хозяев питомцев )
     */
    Collection<OwnerDto> searchOwner(Long requesterId, SearchOwnerDto searchOwnerDto, SearchDirection searchDirection);
}
