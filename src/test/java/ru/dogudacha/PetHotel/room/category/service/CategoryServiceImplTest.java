package ru.dogudacha.PetHotel.room.category.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.room.category.dto.CategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.NewCategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.UpdateCategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.mapper.CategoryMapper;
import ru.dogudacha.PetHotel.room.category.model.Category;
import ru.dogudacha.PetHotel.room.category.repository.CategoryRepository;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CategoryServiceImplTest {
    private final User boss = User.builder()
            .email("boss@pethotel.ru")
            .id(1L)
            .firstName("boss")
            .role(Roles.ROLE_BOSS)
            .build();
    private final User admin = User.builder()
            .email("admin@pethotel.ru")
            .id(2L)
            .firstName("admin")
            .role(Roles.ROLE_ADMIN)
            .build();
    private final User user = User.builder()
            .email("user@pethotel.ru")
            .id(2L)
            .firstName("user")
            .role(Roles.ROLE_USER)
            .build();
    private final User financial = User.builder()
            .email("financial@pethotel.ru")
            .id(2L)
            .firstName("financial")
            .role(Roles.ROLE_FINANCIAL)
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
    private UserRepository userRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    void addCategory_whenAddCategoryByBoss_thenCategoryAdded() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
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
    void addCategory_whenAddCategoryByAdmin_thenCategoryAdded() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategory(any(NewCategoryDto.class))).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.addCategory(admin.getId(), newCategoryDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(categoryDto.getName(), result.getName());
        Assertions.assertEquals(categoryDto.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).save(any(Category.class));
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void addCategory_whenAddCategoryByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.addCategory(user.getId(), newCategoryDto));
    }

    @Test
    void addCategory_whenAddCategoryByFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.addCategory(financial.getId(), newCategoryDto));
    }

    @Test
    void getCategoryById_whenGetCategoryByBoss_thenReturnedCategory() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(boss.getId(), category.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(categoryDto.getName(), result.getName());
        Assertions.assertEquals(categoryDto.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getCategoryById_whenGetCategoryByAdmin_thenReturnedCategory() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.getCategoryById(admin.getId(), category.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(categoryDto.getName(), result.getName());
        Assertions.assertEquals(categoryDto.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getCategoryById_whenGetCategoryByIdByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.getCategoryById(user.getId(), category.getId()));
    }

    @Test
    void getCategoryById_whenGetCategoryByIdByFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.getCategoryById(financial.getId(), category.getId()));
    }

    @Test
    void getCategoryById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.getCategoryById(boss.getId(), category.getId()));
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

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
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
    void updateCategoryById_whenRequesterAdminAndCategoryFound_thenUpdateAllFieldsThanId() {
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

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);
        when(categoryMapper.toCategory(any(UpdateCategoryDto.class))).thenReturn(newCategory);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(updatedCategoryDto);

        CategoryDto result = categoryService.updateCategoryById(admin.getId(), category.getId(), updateCategoryDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(newCategory.getName(), result.getName());
        Assertions.assertEquals(newCategory.getDescription(), result.getDescription());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.updateCategoryById(boss.getId(), category.getId(), new UpdateCategoryDto()));
    }

    @Test
    void updateCategory_whenRequesterFoundAndRoomNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.updateCategoryById(boss.getId(), category.getId(), new UpdateCategoryDto()));
    }

    @Test
    void updateCategory_whenUpdateCategoryByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.updateCategoryById(user.getId(), category.getId(), new UpdateCategoryDto()));
    }

    @Test
    void updateCategory_whenUpdateCategoryByFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.updateCategoryById(financial.getId(), category.getId(), new UpdateCategoryDto()));
    }

    @Test
    void getAllCategories_whenGetAllCategoriesByBoss_thenReturnAllCategories() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toCategoryDto(anyList())).thenReturn(List.of(categoryDto));

        Collection<CategoryDto> resultCollection = categoryService.getAllCategories(boss.getId());
        List<CategoryDto> result = resultCollection.stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(categoryDto.getName(), result.get(0).getName());
        Assertions.assertEquals(categoryDto.getDescription(), result.get(0).getDescription());

        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getAllCategories_whenGetAllCategoriesByAdmin_thenReturnAllCategories() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toCategoryDto(anyList())).thenReturn(List.of(categoryDto));

        Collection<CategoryDto> resultCollection = categoryService.getAllCategories(admin.getId());
        List<CategoryDto> result = resultCollection.stream().toList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(categoryDto.getName(), result.get(0).getName());
        Assertions.assertEquals(categoryDto.getDescription(), result.get(0).getDescription());

        verify(categoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void getAllCategories_whenGetAllCategoriesByUser_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.getAllCategories(user.getId()));
    }

    @Test
    void getAllCategories_whenGetAllCategoriesByFinancial_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.getAllCategories(financial.getId()));
    }

    @Test
    void deleteCategoryId_whenRequesterBossAndCategoryFound_thenCategoryDeleted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(categoryRepository.deleteCategoryById(anyLong())).thenReturn(1);

        categoryService.deleteCategoryById(boss.getId(), category.getId());

        verify(categoryRepository, times(1)).deleteCategoryById(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void deleteCategoryId_whenRequesterAdminAndCategoryFound_thenCategoryDeleted() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(categoryRepository.deleteCategoryById(anyLong())).thenReturn(1);

        categoryService.deleteCategoryById(admin.getId(), category.getId());

        verify(categoryRepository, times(1)).deleteCategoryById(anyLong());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void deleteCategoryId_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> categoryService.deleteCategoryById(boss.getId(), category.getId()));
    }

    @Test
    void deleteCategoryId_whenCategoryNotFound_thenNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(boss));
        when(categoryRepository.deleteCategoryById(anyLong())).thenReturn(0);

        assertThrows(NotFoundException.class,
                () -> categoryService.deleteCategoryById(boss.getId(), category.getId()));
    }

    @Test
    void deleteCategoryId_whenRequesterUserAndCategoryFound_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.deleteCategoryById(user.getId(), category.getId()));
    }

    @Test
    void deleteCategoryId_whenRequesterFinancialAndCategoryFound_thenAccessDeniedException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(financial));

        assertThrows(AccessDeniedException.class,
                () -> categoryService.deleteCategoryById(financial.getId(), category.getId()));
    }
}
