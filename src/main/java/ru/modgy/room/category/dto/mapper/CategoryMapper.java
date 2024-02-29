package ru.modgy.room.category.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.category.dto.NewCategoryDto;
import ru.modgy.room.category.dto.UpdateCategoryDto;
import ru.modgy.room.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category toCategory(CategoryDto categoryDto);

    Category toCategory(NewCategoryDto newCategoryDto);

    Category toCategory(UpdateCategoryDto updateCategoryDto);

    List<CategoryDto> toCategoryDto(List<Category> categories);
}
