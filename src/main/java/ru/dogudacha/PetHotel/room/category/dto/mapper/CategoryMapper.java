package ru.dogudacha.PetHotel.room.category.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dogudacha.PetHotel.room.category.dto.CategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.NewCategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.UpdateCategoryDto;
import ru.dogudacha.PetHotel.room.category.model.Category;

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
