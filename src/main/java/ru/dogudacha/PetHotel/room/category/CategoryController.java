package ru.dogudacha.PetHotel.room.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dogudacha.PetHotel.room.category.dto.CategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.NewCategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.UpdateCategoryDto;
import ru.dogudacha.PetHotel.room.category.service.CategoryService;
import ru.dogudacha.PetHotel.utility.UtilityService;

import java.util.Collection;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                   @RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("CategoryController: POST/addCategory, requesterId={}, Category={}", requesterId, newCategoryDto);
        return categoryService.addCategory(requesterId, newCategoryDto);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                       @PathVariable("id") Long catId) {
        log.info("CategoryController: GET/getCategoryById, requesterId={}, catId={}", requesterId, catId);
        return categoryService.getCategoryById(requesterId, catId);
    }

    @PatchMapping("/{id}")
    public CategoryDto updateCategoryById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                          @RequestBody @Valid UpdateCategoryDto updateCategoryDto,
                                          @PathVariable("id") Long catId) {
        log.info("CategoryController: PATCH/updateCategoryById, requesterId={}, catId={}, requestBody={}",
                requesterId, catId, updateCategoryDto);
        return categoryService.updateCategoryById(requesterId, catId, updateCategoryDto);
    }

    @GetMapping
    public Collection<CategoryDto> getAllCategories(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId) {
        log.info("CategoryController: GET/getAllCategories, requesterId={}", requesterId);
        return categoryService.getAllCategories(requesterId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@RequestHeader(UtilityService.REQUESTER_ID_HEADER) Long requesterId,
                                   @PathVariable("id") Long catId) {
        log.info("CategoryController: DELETE/deleteCategoryById, requesterId={}, catId={}", requesterId, catId);
        categoryService.deleteCategoryById(requesterId, catId);
    }
}
