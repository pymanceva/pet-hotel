package ru.dogudacha.PetHotel.room.category.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.room.category.dto.CategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.NewCategoryDto;
import ru.dogudacha.PetHotel.room.category.dto.UpdateCategoryDto;
import ru.dogudacha.PetHotel.room.category.model.Category;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
public class CategoryServiceIntegrationTest {
    final User requesterAdmin = User.builder()
            .email("admin@mail.ru")
            .name("admin")
            .role(Roles.ROLE_ADMIN)
            .build();
    final NewCategoryDto newCategoryDto = NewCategoryDto.builder()
            .name("Dog room")
            .description("Room for dogs")
            .build();
    final CategoryDto categoryDto = CategoryDto.builder()
            .name("Dog room")
            .description("Room for dogs")
            .build();
    final Category category = Category.builder()
            .name("Dog room")
            .description("Room for dogs")
            .build();
    final UpdateCategoryDto updateCategoryDto = UpdateCategoryDto.builder()
            .name("New name")
            .description("New description")
            .build();
    private final EntityManager em;
    private final CategoryService service;

    @Test
    void addCategory() {
        em.persist(requesterAdmin);
        CategoryDto result = service.addCategory(requesterAdmin.getId(), newCategoryDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(categoryDto.getName()));
        assertThat(result.getDescription(), equalTo(categoryDto.getDescription()));
    }

    @Test
    void getCategoryById() {
        em.persist(requesterAdmin);
        em.persist(category);
        CategoryDto result = service.getCategoryById(requesterAdmin.getId(), category.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(categoryDto.getName()));
        assertThat(result.getDescription(), equalTo(categoryDto.getDescription()));
    }

    @Test
    void updateCategory() {
        em.persist(requesterAdmin);
        em.persist(category);
        CategoryDto result = service.updateCategoryById(requesterAdmin.getId(), category.getId(), updateCategoryDto);

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(updateCategoryDto.getName()));
        assertThat(result.getDescription(), equalTo(updateCategoryDto.getDescription()));
    }

    @Test
    void getAllCategories() {
        em.persist(requesterAdmin);
        em.persist(category);

        List<CategoryDto> result = service.getAllCategories(requesterAdmin.getId()).stream().toList();

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getId(), notNullValue());
        assertThat(result.get(0).getName(), equalTo(categoryDto.getName()));
        assertThat(result.get(0).getDescription(), equalTo(categoryDto.getDescription()));
    }

    @Test
    void deleteCategoryById() {
        em.persist(requesterAdmin);
        em.persist(category);

        service.deleteCategoryById(requesterAdmin.getId(), category.getId());

        String error = String.format("category with id=%d is not found", category.getId());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.getCategoryById(requesterAdmin.getId(), category.getId())
        );

        assertEquals(error, exception.getMessage());
    }
}
