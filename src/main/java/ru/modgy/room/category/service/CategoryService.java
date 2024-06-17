package ru.modgy.room.category.service;

import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.category.dto.NewCategoryDto;
import ru.modgy.room.category.dto.UpdateCategoryDto;

import java.util.Collection;

public interface CategoryService {
    /**
     * Добавление новой категории
     *
     * @param userId         - id пользователя, направляющего запрос
     * @param newCategoryDto - данные добавляемой категории
     * @return данные добавленной категории
     */
    CategoryDto addCategory(Long userId, NewCategoryDto newCategoryDto);

    /**
     * Обновление информации о категории
     *
     * @param userId            - id пользователя, направляющего запрос
     * @param catId             - id обновляемой категории
     * @param updateCategoryDto - обновляемые данные
     * @return данные обновленной категории
     */
    CategoryDto updateCategoryById(Long userId, Long catId, UpdateCategoryDto updateCategoryDto);

    /**
     * Получение по id информации о категории
     *
     * @param userId - id пользователя, направляющего запрос
     * @param catId  - id запрашиваемой категории
     * @return данные запрашиваемой категории
     */
    CategoryDto getCategoryById(Long userId, Long catId);

    /**
     * Получение списка всех категорий
     *
     * @param userId - id пользователя, направляющего запрос
     * @return список категорий
     */
    Collection<CategoryDto> getAllCategories(Long userId);

    /**
     * Удаление по id информации о номере
     *
     * @param userId - id пользователя, направляющего запрос
     * @param catId  - id удаляемой категории
     */
    void deleteCategoryById(Long userId, Long catId);

    /**
     * Проверка названия категории на уникальность.
     *
     * @param userId - id пользователя, направляющего запрос
     * @param categoryName - название, которое проверяется на уникальность в БД
     * @return true - если такое название уникально,
     * false - если название не уникально и в БД имеется запись о категории с таким же названием.
     */
    boolean checkUniqueCategoryName(Long userId, String categoryName);
}
