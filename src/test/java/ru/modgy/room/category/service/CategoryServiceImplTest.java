package ru.modgy.room.category.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.modgy.exception.NotFoundException;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.category.dto.NewCategoryDto;
import ru.modgy.room.category.dto.UpdateCategoryDto;
import ru.modgy.room.category.dto.mapper.CategoryMapper;
import ru.modgy.room.category.model.Category;
import ru.modgy.room.category.repository.CategoryRepository;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;
import ru.modgy.utility.EntityService;
import ru.modgy.utility.UtilityService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceImplTest {
    private final User boss = User.builder()
            .email("boss@pethotel.ru")
            .id(1L)
            .firstName("boss")
            .role(Roles.ROLE_BOSS)
            .isActive(true)
            .build();
    private final User user = User.builder()
            .email("user@pethotel.ru")
            .id(2L)
            .firstName("user")
            .role(Roles.ROLE_USER)
            .isActive(true)
            .build();
    private final NewCategoryDto newCategoryDto = NewCategoryDto.builder()
            .name("Dog room")
            .description("Room for dogs")
            .build();
    long catId = 1L;
    private final Category category = Category.builder()
            .id(catId)
            .name("Dog room")
            .description("Room for dogs")
            .build();
    private final CategoryDto categoryDto = CategoryDto.builder()
            .id(catId)
            .name("Dog room")
            .description("Room for dogs")
            .build();
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UtilityService utilityService;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private EntityService entityService;

    @Test
    void addCategory_whenAddCategoryByBoss_thenCategoryAdded() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategory(any(NewCategoryDto.class))).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.addCategory(boss.getId(), newCategoryDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(categoryDto.getName(), result.getName());
        Assertions.assertEquals(categoryDto.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getCategoryById_whenGetCategoryByBoss_thenReturnedCategory() {
        when(entityService.getCategoryIfExists(anyLong())).thenReturn(category);
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(boss.getId(), category.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(categoryDto.getName(), result.getName());
        Assertions.assertEquals(categoryDto.getDescription(), result.getDescription());

        verify(entityService, times(1)).getCategoryIfExists(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void updateCategoryById_whenRequesterBossAndCategoryFound_thenUpdateAllFieldsThanId() {
        UpdateCategoryDto updateCategoryDto = UpdateCategoryDto.builder()
                .name("new name")
                .description("new description")
                .build();

        CategoryDto updatedCategoryDto = CategoryDto.builder()
                .id(1L)
                .name("new name")
                .description("new description")
                .build();

        Category newCategory = Category.builder()
                .id(1L)
                .name("new name")
                .description("new description")
                .build();

        when(entityService.getCategoryIfExists(anyLong())).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);
        when(categoryMapper.toCategory(any(UpdateCategoryDto.class))).thenReturn(newCategory);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(updatedCategoryDto);

        CategoryDto result = categoryService.updateCategoryById(boss.getId(), category.getId(), updateCategoryDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(newCategory.getName(), result.getName());
        Assertions.assertEquals(newCategory.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void getAllCategories_whenGetAllCategoriesByBoss_thenReturnAllCategories() {
        when(categoryRepository.findAllOrderByNameAsc()).thenReturn(Optional.of(List.of(category)));
        when(categoryMapper.toCategoryDto(anyList())).thenReturn(List.of(categoryDto));

        Collection<CategoryDto> resultCollection = categoryService.getAllCategories(boss.getId());
        List<CategoryDto> result = resultCollection.stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(categoryDto.getName(), result.get(0).getName());
        Assertions.assertEquals(categoryDto.getDescription(), result.get(0).getDescription());

        verify(categoryRepository, times(1)).findAllOrderByNameAsc();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void deleteCategoryId_whenRequesterBossAndCategoryFound_thenCategoryDeleted() {
        when(categoryRepository.deleteCategoryById(anyLong())).thenReturn(1);

        categoryService.deleteCategoryById(boss.getId(), category.getId());

        verify(categoryRepository, times(1)).deleteCategoryById(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void deleteCategoryId_whenRequesterNotFound_thenNotFoundException() {
        doThrow(new NotFoundException(String.format("User with id=%d is not found", user.getId())))
                .when(utilityService).checkBossAdminAccess(anyLong());

        assertThrows(NotFoundException.class,
                () -> categoryService.deleteCategoryById(boss.getId(), category.getId()));
    }

    @Test
    void deleteCategoryId_whenCategoryNotFound_thenNotFoundException() {
        doThrow(new NotFoundException(String.format("Category with id=%d is not found", category.getId())))
                .when(utilityService).checkBossAdminAccess(anyLong());

        assertThrows(NotFoundException.class,
                () -> categoryService.deleteCategoryById(boss.getId(), category.getId()));
    }

    @Test
    void checkUniqueCategoryName_whenNameUnique_thenReturnedTrue() {
        when(categoryRepository.countAllByName(anyString())).thenReturn(0);

        boolean result = categoryService.checkUniqueCategoryName(boss.getId(), category.getName());

        Assertions.assertTrue(result);

        verify(categoryRepository, times(1)).countAllByName(anyString());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void checkUniqueCategoryName_whenNameNotUnique_thenReturnedFalse() {
        when(categoryRepository.countAllByName(anyString())).thenReturn(1);

        boolean result = categoryService.checkUniqueCategoryName(boss.getId(), category.getName());

        Assertions.assertFalse(result);

        verify(categoryRepository, times(1)).countAllByName(anyString());
        verifyNoMoreInteractions(categoryRepository);
    }
}
