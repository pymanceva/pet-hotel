package ru.dogudacha.PetHotel.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.user.dto.UpdateUserDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.service.api.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    long userId = 1L;
    UserDto userDto = new UserDto(userId, "userName", "user@mail.ru", Roles.ROLE_ADMIN);

    @Test
    @SneakyThrows
    void addUser() {
        when(userService.addUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.role", is(userDto.getRole().toString())));

        verify(userService).addUser(any(UserDto.class));

        mockMvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserDto())))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).addUser(any(UserDto.class));
    }

    @Test
    @SneakyThrows
    void getUserById() {
        when(userService.getUserById(anyLong())).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.role", is(userDto.getRole().toString())));

        verify(userService).getUserById(userId);

        when(userService.getUserById(anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/users/{userId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService, times(2)).getUserById(userId);
    }

    @Test
    @SneakyThrows
    void updateUser() {
        when(userService.updateUser(anyLong(), any(UpdateUserDto.class))).thenReturn(userDto);

        mockMvc.perform(patch("/users/{id}", 1)
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.role", is(userDto.getRole().toString())));


        when(userService.updateUser(anyLong(), any(UpdateUserDto.class))).thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/users/{userId}", 0)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());

    }

    @Test
    @SneakyThrows
    void getAllUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(userDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.[0].role", is(userDto.getRole().toString())));

    }

    @Test
    @SneakyThrows
    void deleteUserById() {
        mockMvc.perform(delete("/users/{userId}", 1)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(anyLong());

        doThrow(NotFoundException.class)
                .when(userService)
                .deleteUserById(anyLong());

        mockMvc.perform(delete("/users/{userId}", 1)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNotFound());

        verify(userService, times(2)).deleteUserById(anyLong());
    }
}