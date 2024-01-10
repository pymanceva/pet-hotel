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
import ru.dogudacha.PetHotel.user.controller.UserController;
import ru.dogudacha.PetHotel.user.dto.NewUserDto;
import ru.dogudacha.PetHotel.user.dto.UpdateUserDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.service.UserService;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    long userId = 2L;
    long requesterId = 1L;
    private final String requesterHeader = "X-PetHotel-User-Id";
    UserDto userDto = new UserDto(userId, "userLastName", "userFirstName", "userMiddleName",
            "user@mail.ru", "user_pwd", Roles.ROLE_ADMIN, true);

    @Test
    @SneakyThrows
    void addUser() {
        when(userService.addUser(anyLong(), any(NewUserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(userDto.getMiddleName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.password", is(userDto.getPassword())))
                .andExpect(jsonPath("$.role", is(userDto.getRole().toString())))
                .andExpect(jsonPath("$.isActive", is(userDto.getIsActive())));

        verify(userService).addUser(anyLong(), any(NewUserDto.class));

        mockMvc.perform(post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new NewUserDto())))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/users")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new NewUserDto())))
                .andExpect(status().isBadRequest());

        verify(userService, times(1)).addUser(anyLong(), any(NewUserDto.class));
    }

    @Test
    @SneakyThrows
    void getUserById() {
        when(userService.getUserById(anyLong(), anyLong())).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", userId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(userDto.getMiddleName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.password", is(userDto.getPassword())))
                .andExpect(jsonPath("$.role", is(userDto.getRole().toString())))
                .andExpect(jsonPath("$.isActive", is(userDto.getIsActive())));

        verify(userService).getUserById(requesterId, userId);

        when(userService.getUserById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/users/{userId}", userId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(userService, times(2)).getUserById(requesterId, userId);
    }

    @Test
    @SneakyThrows
    void updateUser() {
        when(userService.updateUser(anyLong(), eq(userId), any(UpdateUserDto.class))).thenReturn(userDto);

        mockMvc.perform(patch("/users/{id}", userId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(userDto.getMiddleName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.password", is(userDto.getPassword())))
                .andExpect(jsonPath("$.role", is(userDto.getRole().toString())))
                .andExpect(jsonPath("$.isActive", is(userDto.getIsActive())));


        when(userService.updateUser(anyLong(), eq(userId), any(UpdateUserDto.class)))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .header(requesterHeader, requesterId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());

    }

    @Test
    @SneakyThrows
    void getAllUsers() {
        when(userService.getAllUsers(anyLong())).thenReturn(List.of(userDto));

        mockMvc.perform(get("/users")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].lastName", is(userDto.getLastName())))
                .andExpect(jsonPath("$.[0].firstName", is(userDto.getFirstName())))
                .andExpect(jsonPath("$.[0].middleName", is(userDto.getMiddleName())))
                .andExpect(jsonPath("$.[0].email", is(userDto.getEmail())))
                .andExpect(jsonPath("$.[0].password", is(userDto.getPassword())))
                .andExpect(jsonPath("$.[0].role", is(userDto.getRole().toString())))
                .andExpect(jsonPath("$.[0].isActive", is(userDto.getIsActive())));
    }

    @Test
    @SneakyThrows
    void deleteUserById() {
        mockMvc.perform(delete("/users/{userId}", userId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(requesterId, userId);

        doThrow(NotFoundException.class)
                .when(userService)
                .deleteUserById(requesterId, userId);

        mockMvc.perform(delete("/users/{userId}", userId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNotFound());

        verify(userService, times(2)).deleteUserById(requesterId, userId);
    }

    @Test
    @SneakyThrows
    void setUserState() {
        UserDto activeUserDto = userDto;
        activeUserDto.setIsActive(true);
        when(userService.setUserState(anyLong(), eq(userId), eq(true))).thenReturn(activeUserDto);

        mockMvc.perform(patch("/users/{id}/state", userId)
                        .header(requesterHeader, requesterId)
                        .param("isActive", String.valueOf(true))
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(activeUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(activeUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastName", is(activeUserDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(activeUserDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(activeUserDto.getMiddleName())))
                .andExpect(jsonPath("$.email", is(activeUserDto.getEmail())))
                .andExpect(jsonPath("$.password", is(activeUserDto.getPassword())))
                .andExpect(jsonPath("$.role", is(activeUserDto.getRole().toString())))
                .andExpect(jsonPath("$.isActive", is(activeUserDto.getIsActive())));

        UserDto notActiveUserDto = userDto;
        notActiveUserDto.setIsActive(false);
        when(userService.setUserState(anyLong(), eq(userId), eq(false))).thenReturn(notActiveUserDto);

        mockMvc.perform(patch("/users/{id}/state", userId)
                        .header(requesterHeader, requesterId)
                        .param("isActive", String.valueOf(false))
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(notActiveUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notActiveUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastName", is(notActiveUserDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(notActiveUserDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(notActiveUserDto.getMiddleName())))
                .andExpect(jsonPath("$.email", is(notActiveUserDto.getEmail())))
                .andExpect(jsonPath("$.password", is(notActiveUserDto.getPassword())))
                .andExpect(jsonPath("$.role", is(notActiveUserDto.getRole().toString())))
                .andExpect(jsonPath("$.isActive", is(false)));

        when(userService.setUserState(anyLong(), eq(userId), eq(true)))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/users/{userId}/state", userId)
                        .header(requesterHeader, requesterId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());

    }
}