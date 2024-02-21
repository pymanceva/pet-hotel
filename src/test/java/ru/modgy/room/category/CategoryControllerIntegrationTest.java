package ru.modgy.room.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.modgy.exception.NotFoundException;
import ru.modgy.room.category.dto.CategoryDto;
import ru.modgy.room.category.dto.NewCategoryDto;
import ru.modgy.room.category.dto.UpdateCategoryDto;
import ru.modgy.room.category.service.CategoryService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
public class CategoryControllerIntegrationTest {
    private final String requesterHeader = "X-PetHotel-User-Id";
    long requesterId = 1L;
    long catId = 1L;
    private final CategoryDto categoryDto = CategoryDto.builder()
            .id(catId)
            .name("Dog room")
            .description("Room for dogs")
            .build();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;

    @Test
    @SneakyThrows
    void addCategory() {
        when(categoryService.addCategory(anyLong(), any(NewCategoryDto.class))).thenReturn(categoryDto);

        mockMvc.perform(post("/categories")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())))
                .andExpect(jsonPath("$.description", is(categoryDto.getDescription())));

        verify(categoryService).addCategory(anyLong(), any(NewCategoryDto.class));

        mockMvc.perform(post("/categories")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new NewCategoryDto())))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/categories")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new NewCategoryDto())))
                .andExpect(status().isBadRequest());

        verify(categoryService, times(1)).addCategory(anyLong(), any(NewCategoryDto.class));
    }

    @Test
    @SneakyThrows
    void getCategoryById() {
        when(categoryService.getCategoryById(anyLong(), anyLong())).thenReturn(categoryDto);

        mockMvc.perform(get("/categories/{id}", catId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())))
                .andExpect(jsonPath("$.description", is(categoryDto.getDescription())));

        verify(categoryService).getCategoryById(requesterId, catId);

        when(categoryService.getCategoryById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/categories/{id}", catId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(categoryService, times(2)).getCategoryById(requesterId, catId);
    }

    @Test
    @SneakyThrows
    void updateCategoryById() {
        when(categoryService.updateCategoryById(anyLong(), eq(catId), any(UpdateCategoryDto.class)))
                .thenReturn(categoryDto);

        mockMvc.perform(patch("/categories/{id}", catId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(categoryDto.getName())))
                .andExpect(jsonPath("$.description", is(categoryDto.getDescription())));


        when(categoryService.updateCategoryById(anyLong(), eq(catId), any(UpdateCategoryDto.class)))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/categories/{id}", catId)
                        .header(requesterHeader, requesterId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void getAllCategories() {
        when(categoryService.getAllCategories(anyLong())).thenReturn(List.of(categoryDto));

        mockMvc.perform(get("/categories")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(categoryDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(categoryDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(categoryDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void deleteCategoryById() {
        mockMvc.perform(delete("/categories/{Id}", catId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNoContent());

        verify(categoryService).deleteCategoryById(requesterId, catId);

        doThrow(NotFoundException.class)
                .when(categoryService)
                .deleteCategoryById(requesterId, catId);

        mockMvc.perform(delete("/categories/{Id}", catId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNotFound());

        verify(categoryService, times(2)).deleteCategoryById(requesterId, catId);
    }
}
