package ru.dogudacha.PetHotel.owner;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.owner.controller.OwnerController;
import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;
import ru.dogudacha.PetHotel.owner.service.OwnerService;
import ru.dogudacha.PetHotel.user.UserController;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = OwnerController.class)
class OwnerControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private OwnerService ownerService;


    long ownerId = 2L;
    long requesterId = 1L;
    private final String requesterHeader = "X-PetHotel-User-Id";
    OwnerDto ownerDto = new OwnerDto(ownerId, "ownerName", "owner@mail.ru");

    @Test
    @SneakyThrows
    void addOwner() {
        when(ownerService.addOwner(anyLong(), any(OwnerDto.class))).thenReturn(ownerDto);

        mockMvc.perform(post("/owners")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(ownerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(ownerDto.getName())))
                .andExpect(jsonPath("$.email", is(ownerDto.getEmail())));

        verify(ownerService).addOwner(anyLong(), any(OwnerDto.class));

        mockMvc.perform(post("/owners")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new OwnerDto())))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/owners")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new OwnerDto())))
                .andExpect(status().isBadRequest());

        verify(ownerService, times(1)).addOwner(anyLong(), any(OwnerDto.class));
    }

    @Test
    @SneakyThrows
    void getOwnerById() {
        when(ownerService.getOwnerById(anyLong(), anyLong())).thenReturn(ownerDto);

        mockMvc.perform(get("/owners/{id}", ownerId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(ownerDto.getName())))
                .andExpect(jsonPath("$.email", is(ownerDto.getEmail())));

        verify(ownerService).getOwnerById(requesterId, ownerId);

        when(ownerService.getOwnerById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/owners/{ownerId}", ownerId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(ownerService, times(2)).getOwnerById(requesterId, ownerId);
    }

    @Test
    @SneakyThrows
    void updateOwner() {
        when(ownerService.updateOwner(anyLong(), eq(ownerId), any(UpdateOwnerDto.class))).thenReturn(ownerDto);

        mockMvc.perform(patch("/owners/{id}", ownerId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(ownerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(ownerDto.getName())))
                .andExpect(jsonPath("$.email", is(ownerDto.getEmail())));


        when(ownerService.updateOwner(anyLong(), eq(ownerId), any(UpdateOwnerDto.class)))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(patch("/owners/{ownerId}", ownerId)
                        .header(requesterHeader, requesterId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(ownerDto)))
                .andExpect(status().isNotFound());

    }

    @Test
    @SneakyThrows
    void getAllOwners() {
        when(ownerService.getAllOwners(anyLong())).thenReturn(List.of(ownerDto));

        mockMvc.perform(get("/owners")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(ownerDto.getName())))
                .andExpect(jsonPath("$.[0].email", is(ownerDto.getEmail())));
    }

    @Test
    @SneakyThrows
    void deleteOwnerById() {
        mockMvc.perform(delete("/owners/{ownerId}", ownerId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNoContent());

        verify(ownerService).deleteOwnerById(requesterId, ownerId);

        doThrow(NotFoundException.class)
                .when(ownerService)
                .deleteOwnerById(requesterId, ownerId);

        mockMvc.perform(delete("/owners/{ownerId}", ownerId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE))
                .andExpect(status().isNotFound());

        verify(ownerService, times(2)).deleteOwnerById(requesterId, ownerId);
    }
}