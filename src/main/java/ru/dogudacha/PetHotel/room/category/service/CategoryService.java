package ru.dogudacha.PetHotel.room.category.service;

import ru.dogudacha.PetHotel.room.category.dto.CategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.NewCategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.UpdateCategoryDto;

import java.util.Collection;

public interface CategoryService {
    CategoryDto addCategory(Long userId, NewCategoryDto newCategoryDto);

    CategoryDto updateCategoryById(Long userId, Long catId, UpdateCategoryDto updateCategoryDto);

    CategoryDto getCategoryById(Long userId, Long catId);

    Collection<CategoryDto> getAllCategories(Long userId);

    void deleteCategoryById(Long userId, Long catId);
}
