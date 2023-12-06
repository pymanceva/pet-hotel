package ru.dogudacha.PetHotel.pet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.controller.PetController;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfPet;
import ru.dogudacha.PetHotel.pet.service.PetService;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PetController.class)
public class PetControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    private final String requesterHeader = "X-PetHotel-User-Id";

    final User requesterBoss = User.builder()
            .email("boss@mail.ru")
            .name("boss")
            .id(1L)
            .role(Roles.ROLE_BOSS)
            .build();

    final User requesterAdmin = User.builder()
            .email("admin@mail.ru")
            .name("admin")
            .id(2L)
            .role(Roles.ROLE_ADMIN)
            .build();

    final User requesterUser = User.builder()
            .email("user@mail.ru")
            .name("user")
            .id(3L)
            .role(Roles.ROLE_USER)
            .build();

    final PetDto petDto = PetDto.builder()
            .id(1L)
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age("2 года")
            .build();


    final UpdatePetDto updatePet = UpdatePetDto.builder()
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .build();

    final PetDto updatedPet = PetDto.builder()
            .id(1L)
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .build();


    @Test
    @SneakyThrows
    void addPet() {
        when(petService.addPet(any(), any(NewPetDto.class))).thenReturn(petDto);

        mockMvc.perform(post("/pets")
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.typeOfPet", is(petDto.getTypeOfPet())))
                .andExpect(jsonPath("$.breed", is(petDto.getBreed())))
                .andExpect(jsonPath("$.sex", is(petDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petDto.getAge())));

        mockMvc.perform(post("/pets")
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.typeOfPet", is(petDto.getTypeOfPet())))
                .andExpect(jsonPath("$.breed", is(petDto.getBreed())))
                .andExpect(jsonPath("$.sex", is(petDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petDto.getAge())));


        String errorAccessDenied = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());
        when(petService.addPet(anyLong(), any())).thenThrow(new AccessDeniedException(errorAccessDenied));

        mockMvc.perform(post("/pets")
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andDo(print())
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/pets")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/pets")
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new PetDto())))
                .andExpect(status().isBadRequest());

        verify(petService, times(3)).addPet(anyLong(), any(NewPetDto.class));
    }

    @Test
    @SneakyThrows
    void getPetById() {
        when(petService.getPetById(anyLong(), anyLong())).thenReturn(petDto);

        mockMvc.perform(get("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.typeOfPet", is(petDto.getTypeOfPet())))
                .andExpect(jsonPath("$.breed", is(petDto.getBreed())))
                .andExpect(jsonPath("$.sex", is(petDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petDto.getAge())));

        mockMvc.perform(get("/pets/{id}", petDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        String errorNotFoundPet = String.format("Pet with id = %d not found", petDto.getId());
        when(petService.getPetById(anyLong(), eq(0L))).thenThrow(new NotFoundException(errorNotFoundPet));

        mockMvc.perform(get("/pets/{id}/admin", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(petService, times(2)).getPetById(anyLong(), anyLong());
    }


    @Test
    @SneakyThrows
    void updatePet() {
        when(petService.updatePet(anyLong(), anyLong(), any())).thenReturn(updatedPet);

        mockMvc.perform(patch("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.typeOfPet", is(updatePet.getTypeOfPet())))
                .andExpect(jsonPath("$.breed", is(updatePet.getBreed())))
                .andExpect(jsonPath("$.sex", is(updatePet.getSex().toString())))
                .andExpect(jsonPath("$.age", is(updatePet.getAge())));

        mockMvc.perform(patch("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.typeOfPet", is(updatePet.getTypeOfPet())))
                .andExpect(jsonPath("$.breed", is(updatePet.getBreed())))
                .andExpect(jsonPath("$.sex", is(updatePet.getSex().toString())))
                .andExpect(jsonPath("$.age", is(updatePet.getAge())));


        String errorAccessDenied = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());
        when(petService.updatePet(anyLong(), eq(petDto.getId()), any())).thenThrow(new AccessDeniedException(errorAccessDenied));

        mockMvc.perform(patch("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isForbidden());

        String errorNotFoundPet = String.format("Pet with id = %d not found", petDto.getId());
        when(petService.updatePet(anyLong(), eq(0L), any())).thenThrow(new NotFoundException(errorNotFoundPet));

        mockMvc.perform(patch("/pets/{id}", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(patch("/pets/{id}", petDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new PetDto())))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(petService, times(4)).updatePet(anyLong(), anyLong(), any(UpdatePetDto.class));
    }


    @Test
    @SneakyThrows
    void deletePet() {
        doNothing().when(petService).deletePetById(anyLong(), anyLong());

        mockMvc.perform(delete("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        String errorNotFoundPet = String.format("Pet with id = %d not found", petDto.getId());
        doThrow(new NotFoundException(errorNotFoundPet)).when(petService).deletePetById(anyLong(), anyLong());

        mockMvc.perform(delete("/pets/{id}", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        String errorAccessDenied = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());
        doThrow(new AccessDeniedException(errorAccessDenied)).when(petService).deletePetById(anyLong(), anyLong());

        mockMvc.perform(delete("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/pets/{id}", petDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());


        verify(petService, times(4)).deletePetById(anyLong(), anyLong());
    }


}
