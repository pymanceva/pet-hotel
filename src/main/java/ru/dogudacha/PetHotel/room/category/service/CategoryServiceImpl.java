package ru.dogudacha.PetHotel.room.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.room.category.dto.CategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.NewCategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.UpdateCategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.mapper.CategoryMapper;
import ru.dogudacha.PetHotel.room.category.model.Category;
import ru.dogudacha.PetHotel.room.category.repository.CategoryRepository;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    final private CategoryRepository categoryRepository;
    final private CategoryMapper categoryMapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CategoryDto addCategory(Long userId, NewCategoryDto newCategoryDto) {
        checkAdminAccess(userId);

        Category newCategory = categoryMapper.toCategory(newCategoryDto);
        Category addedCategory = categoryRepository.save(newCategory);
        log.info("CategoryService: addCategory, userId={}, newCategoryDto={}", userId, newCategoryDto);
        return categoryMapper.toCategoryDto(addedCategory);
    }

    @Transactional
    @Override
    public CategoryDto updateCategoryById(Long userId, Long catId, UpdateCategoryDto updateCategoryDto) {
        checkAdminAccess(userId);

        Category oldCategory = findCategoryById(catId);
        Category newCategory = categoryMapper.toCategory(updateCategoryDto);
        newCategory.setId(oldCategory.getId());

        if (Objects.isNull(newCategory.getName())) {
            newCategory.setName(oldCategory.getName());
        }

        if (Objects.isNull(newCategory.getDescription())) {
            newCategory.setDescription(oldCategory.getDescription());
        }

        Category updatedCategory = categoryRepository.save(newCategory);
        log.info("CategoryService: updateCategoryById, userId={}, catId={}, updateCategoryDto={}",
                userId, catId, updateCategoryDto);

        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategoryById(Long userId, Long catId) {
        checkAdminAccess(userId);

        Category category = findCategoryById(catId);
        log.info("CategoryService: getCategoryById, userId={}, catId={}", userId, catId);
        return categoryMapper.toCategoryDto(category);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<CategoryDto> getAllCategories(Long userId) {
        checkAdminAccess(userId);

        List<Category> allCategories = categoryRepository.findAll();
        log.info("CategoryService: getAllCategories, userId={}, list size={}", userId, allCategories.size());
        return categoryMapper.toCategoryDto(allCategories);
    }

    @Transactional
    @Override
    public void deleteCategoryById(Long userId, Long catId) {
        checkAdminAccess(userId);

        int result = categoryRepository.deleteCategoryById(catId);

        if (result == 0) {
            throw new NotFoundException(String.format("category with id=%d not found", catId));
        }

        log.info("CategoryService: deleteCategoryById, userId={}, catId={}", userId, catId);
    }

    //@Named("idToCategory")
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("category with id=%d is not found", id)));
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("user with id=%d is not found", userId)));
    }

    private void checkAdminAccess(Long userId) {
        User user = findUserById(userId);

        if (user.getRole().ordinal() >= 2) {
            throw new AccessDeniedException(String.format("User with role=%s, can't access for this action",
                    user.getRole()));
        }
    }
}
