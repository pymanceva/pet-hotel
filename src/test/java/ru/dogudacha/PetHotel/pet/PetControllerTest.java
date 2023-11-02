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
import ru.dogudacha.PetHotel.pet.dto.PetForAdminDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfDiet;
import ru.dogudacha.PetHotel.pet.service.PetService;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;

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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

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
            .typeOfPet("Dog")
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age(2)
            .weight(7)
            .diet(TypeOfDiet.READY_INDUSTRIAL_FOOD)
            .isTakesMedications(false)
            .isContact(true)
            .isPhotographed(true)
            .comments("Like play with ball.")
            .build();

    final PetForAdminDto petForAdminDto = PetForAdminDto.builder()
            .id(1L)
            .typeOfPet("Dog")
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age(2)
            .weight(7)
            .diet(TypeOfDiet.READY_INDUSTRIAL_FOOD)
            .isTakesMedications(false)
            .isContact(true)
            .isPhotographed(true)
            .comments("Like play with ball.")
            .historyOfBookings(null)
            .additionalServices(null)
            .build();


    final UpdatePetDto updatePet = UpdatePetDto.builder()
            .typeOfPet("Dog small")
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .age(12)
            .weight(8)
            .diet(TypeOfDiet.NATURAL_RAW_FOOD)
            .isTakesMedications(true)
            .isContact(false)
            .isPhotographed(false)
            .comments("Like play with small ball.")
            .build();

    final PetDto updatedPet = PetDto.builder()
            .id(1L)
            .typeOfPet("Dog small")
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .age(12)
            .weight(8)
            .diet(TypeOfDiet.NATURAL_RAW_FOOD)
            .isTakesMedications(true)
            .isContact(false)
            .isPhotographed(false)
            .comments("Like play with small ball.")
            .build();

//    @Test
//    @SneakyThrows
//    void addPet() {
//        when(petService.addPet(any(), any(NewPetDto.class))).thenReturn(petDto);
//
//        mockMvc.perform(post("/pets")
//                        .header(requesterHeader, requesterBoss.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(petDto)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()))
//                .andExpect(jsonPath("$.typeOfPet", is(petForAdminDto.getTypeOfPet())))
//                .andExpect(jsonPath("$.breed", is(petForAdminDto.getBreed())))
//                .andExpect(jsonPath("$.sex", is(petForAdminDto.getSex().toString())))
//                .andExpect(jsonPath("$.age", is(petForAdminDto.getAge())))
//                .andExpect(jsonPath("$.weight", is(petForAdminDto.getWeight())))
//                .andExpect(jsonPath("$.diet", is(petForAdminDto.getDiet().toString())))
//                .andExpect(jsonPath("$.isTakesMedications", is(petForAdminDto.getIsTakesMedications())))
//                .andExpect(jsonPath("$.isContact", is(petForAdminDto.getIsContact())))
//                .andExpect(jsonPath("$.isPhotographed", is(petForAdminDto.getIsPhotographed())))
//                .andExpect(jsonPath("$.comments", is(petForAdminDto.getComments())));
//
//        mockMvc.perform(post("/pets")
//                        .header(requesterHeader, requesterAdmin.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(petDto)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", notNullValue()))
//                .andExpect(jsonPath("$.typeOfPet", is(petForAdminDto.getTypeOfPet())))
//                .andExpect(jsonPath("$.breed", is(petForAdminDto.getBreed())))
//                .andExpect(jsonPath("$.sex", is(petForAdminDto.getSex().toString())))
//                .andExpect(jsonPath("$.age", is(petForAdminDto.getAge())))
//                .andExpect(jsonPath("$.weight", is(petForAdminDto.getWeight())))
//                .andExpect(jsonPath("$.diet", is(petForAdminDto.getDiet().toString())))
//                .andExpect(jsonPath("$.isTakesMedications", is(petForAdminDto.getIsTakesMedications())))
//                .andExpect(jsonPath("$.isContact", is(petForAdminDto.getIsContact())))
//                .andExpect(jsonPath("$.isPhotographed", is(petForAdminDto.getIsPhotographed())))
//                .andExpect(jsonPath("$.comments", is(petForAdminDto.getComments())));
//
//
//        String errorAccessDenied = String.format("User with role = %s, can't access for this action",
//                requesterUser.getRole());
//        when(petService.addPet(anyLong(), any())).thenThrow(new AccessDeniedException(errorAccessDenied));
//
//        mockMvc.perform(post("/pets")
//                        .header(requesterHeader, requesterUser.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(petDto)))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//
//        mockMvc.perform(post("/pets")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(petDto)))
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(post("/pets")
//                        .header(requesterHeader, requesterBoss.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(new PetDto())))
//                .andExpect(status().isBadRequest());
//
//        verify(petService, times(3)).addPet(anyLong(), any(NewPetDto.class));
//    }

    @Test
    @SneakyThrows
    void getPetById() {
        when(petService.getPetByIdForAdmin(anyLong(), anyLong())).thenReturn(petForAdminDto);

        mockMvc.perform(get("/pets/{id}/admin", petDto.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.typeOfPet", is(petForAdminDto.getTypeOfPet())))
                .andExpect(jsonPath("$.breed", is(petForAdminDto.getBreed())))
                .andExpect(jsonPath("$.sex", is(petForAdminDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petForAdminDto.getAge())))
                .andExpect(jsonPath("$.weight", is(petForAdminDto.getWeight())))
                .andExpect(jsonPath("$.diet", is(petForAdminDto.getDiet().toString())))
                .andExpect(jsonPath("$.isTakesMedications", is(petForAdminDto.getIsTakesMedications())))
                .andExpect(jsonPath("$.isContact", is(petForAdminDto.getIsContact())))
                .andExpect(jsonPath("$.isPhotographed", is(petForAdminDto.getIsPhotographed())))
                .andExpect(jsonPath("$.comments", is(petForAdminDto.getComments())))
                .andExpect(jsonPath("$.historyOfBookings", is(petForAdminDto.getHistoryOfBookings())))
                .andExpect(jsonPath("$.additionalServices", is(petForAdminDto.getAdditionalServices())));

        mockMvc.perform(get("/pets/{id}/admin", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.typeOfPet", is(petForAdminDto.getTypeOfPet())))
                .andExpect(jsonPath("$.breed", is(petForAdminDto.getBreed())))
                .andExpect(jsonPath("$.sex", is(petForAdminDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petForAdminDto.getAge())))
                .andExpect(jsonPath("$.weight", is(petForAdminDto.getWeight())))
                .andExpect(jsonPath("$.diet", is(petForAdminDto.getDiet().toString())))
                .andExpect(jsonPath("$.isTakesMedications", is(petForAdminDto.getIsTakesMedications())))
                .andExpect(jsonPath("$.isContact", is(petForAdminDto.getIsContact())))
                .andExpect(jsonPath("$.isPhotographed", is(petForAdminDto.getIsPhotographed())))
                .andExpect(jsonPath("$.comments", is(petForAdminDto.getComments())))
                .andExpect(jsonPath("$.historyOfBookings", is(petForAdminDto.getHistoryOfBookings())))
                .andExpect(jsonPath("$.additionalServices", is(petForAdminDto.getAdditionalServices())));

        when(petService.getPetByIdForUser(anyLong(), anyLong())).thenReturn(petDto);

        mockMvc.perform(get("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.typeOfPet", is(petDto.getTypeOfPet())))
                .andExpect(jsonPath("$.breed", is(petDto.getBreed())))
                .andExpect(jsonPath("$.sex", is(petDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petDto.getAge())))
                .andExpect(jsonPath("$.weight", is(petDto.getWeight())))
                .andExpect(jsonPath("$.diet", is(petDto.getDiet().toString())))
                .andExpect(jsonPath("$.isTakesMedications", is(petDto.getIsTakesMedications())))
                .andExpect(jsonPath("$.isContact", is(petDto.getIsContact())))
                .andExpect(jsonPath("$.isPhotographed", is(petDto.getIsPhotographed())))
                .andExpect(jsonPath("$.comments", is(petDto.getComments())));

        mockMvc.perform(get("/pets/{id}", petDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        String errorNotFoundPet = String.format("Pet with id = %d not found", petDto.getId());
        when(petService.getPetByIdForAdmin(anyLong(), eq(0L))).thenThrow(new NotFoundException(errorNotFoundPet));

        mockMvc.perform(get("/pets/{id}/admin", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(petService, times(3)).getPetByIdForAdmin(anyLong(), anyLong());
    }

//    @Test
//    @SneakyThrows
//    void updatePet() {
//        when(petService.updatePet(anyLong(), anyLong(), any())).thenReturn(updatedPet);
//
//        mockMvc.perform(patch("/pets/{id}", petDto.getId())
//                        .header(requesterHeader, requesterAdmin.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(updatePet)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()))
//                .andExpect(jsonPath("$.typeOfPet", is(updatePet.getTypeOfPet())))
//                .andExpect(jsonPath("$.breed", is(updatePet.getBreed())))
//                .andExpect(jsonPath("$.sex", is(updatePet.getSex().toString())))
//                .andExpect(jsonPath("$.age", is(updatePet.getAge())))
//                .andExpect(jsonPath("$.weight", is(updatePet.getWeight())))
//                .andExpect(jsonPath("$.diet", is(updatePet.getDiet().toString())))
//                .andExpect(jsonPath("$.isTakesMedications", is(updatePet.getIsTakesMedications())))
//                .andExpect(jsonPath("$.isContact", is(updatePet.getIsContact())))
//                .andExpect(jsonPath("$.isPhotographed", is(updatePet.getIsPhotographed())))
//                .andExpect(jsonPath("$.comments", is(updatePet.getComments())));
//
//        mockMvc.perform(patch("/pets/{id}", petDto.getId())
//                        .header(requesterHeader, requesterBoss.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(updatePet)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", notNullValue()))
//                .andExpect(jsonPath("$.typeOfPet", is(updatePet.getTypeOfPet())))
//                .andExpect(jsonPath("$.breed", is(updatePet.getBreed())))
//                .andExpect(jsonPath("$.sex", is(updatePet.getSex().toString())))
//                .andExpect(jsonPath("$.age", is(updatePet.getAge())))
//                .andExpect(jsonPath("$.weight", is(updatePet.getWeight())))
//                .andExpect(jsonPath("$.diet", is(updatePet.getDiet().toString())))
//                .andExpect(jsonPath("$.isTakesMedications", is(updatePet.getIsTakesMedications())))
//                .andExpect(jsonPath("$.isContact", is(updatePet.getIsContact())))
//                .andExpect(jsonPath("$.isPhotographed", is(updatePet.getIsPhotographed())))
//                .andExpect(jsonPath("$.comments", is(updatePet.getComments())));
//
//
//        String errorAccessDenied = String.format("User with role = %s, can't access for this action",
//                requesterUser.getRole());
//        when(petService.updatePet(anyLong(), eq(petDto.getId()), any())).thenThrow(new AccessDeniedException(errorAccessDenied));
//
//        mockMvc.perform(patch("/pets/{id}", petDto.getId())
//                        .header(requesterHeader, requesterUser.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(updatePet)))
//                .andDo(print())
//                .andExpect(status().isForbidden());
//
//        String errorNotFoundPet = String.format("Pet with id = %d not found", petDto.getId());
//        when(petService.updatePet(anyLong(), eq(0L), any())).thenThrow(new NotFoundException(errorNotFoundPet));
//
//        mockMvc.perform(patch("/pets/{id}", 0L)
//                        .header(requesterHeader, requesterBoss.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(updatePet)))
//                .andDo(print())
//                .andExpect(status().isNotFound());
//
//        mockMvc.perform(patch("/pets/{id}", petDto.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(updatePet)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//        mockMvc.perform(patch("/pets/{id}", petDto.getId())
//                        .header(requesterHeader, requesterBoss.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsString(new PetDto())))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//        verify(petService, times(4)).updatePet(anyLong(), anyLong(), any(UpdatePetDto.class));
//    }

    @Test
    @SneakyThrows
    void getAllPets() {
        when(petService.getAllPetsForAdmin(anyLong())).thenReturn(List.of(petForAdminDto));

        mockMvc.perform(get("/pets")
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].typeOfPet", is(petForAdminDto.getTypeOfPet())))
                .andExpect(jsonPath("$[0].breed", is(petForAdminDto.getBreed())))
                .andExpect(jsonPath("$[0].sex", is(petForAdminDto.getSex().toString())))
                .andExpect(jsonPath("$[0].age", is(petForAdminDto.getAge())))
                .andExpect(jsonPath("$[0].weight", is(petForAdminDto.getWeight())))
                .andExpect(jsonPath("$[0].diet", is(petForAdminDto.getDiet().toString())))
                .andExpect(jsonPath("$[0].isTakesMedications", is(petForAdminDto.getIsTakesMedications())))
                .andExpect(jsonPath("$[0].isContact", is(petForAdminDto.getIsContact())))
                .andExpect(jsonPath("$[0].isPhotographed", is(petForAdminDto.getIsPhotographed())))
                .andExpect(jsonPath("$[0].comments", is(petForAdminDto.getComments())))
                .andExpect(jsonPath("$[0].historyOfBookings", is(petForAdminDto.getHistoryOfBookings())))
                .andExpect(jsonPath("$[0].additionalServices", is(petForAdminDto.getAdditionalServices())));

        mockMvc.perform(get("/pets")
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].typeOfPet", is(petForAdminDto.getTypeOfPet())))
                .andExpect(jsonPath("$[0].breed", is(petForAdminDto.getBreed())))
                .andExpect(jsonPath("$[0].sex", is(petForAdminDto.getSex().toString())))
                .andExpect(jsonPath("$[0].age", is(petForAdminDto.getAge())))
                .andExpect(jsonPath("$[0].weight", is(petForAdminDto.getWeight())))
                .andExpect(jsonPath("$[0].diet", is(petForAdminDto.getDiet().toString())))
                .andExpect(jsonPath("$[0].isTakesMedications", is(petForAdminDto.getIsTakesMedications())))
                .andExpect(jsonPath("$[0].isContact", is(petForAdminDto.getIsContact())))
                .andExpect(jsonPath("$[0].isPhotographed", is(petForAdminDto.getIsPhotographed())))
                .andExpect(jsonPath("$[0].comments", is(petForAdminDto.getComments())))
                .andExpect(jsonPath("$[0].historyOfBookings", is(petForAdminDto.getHistoryOfBookings())))
                .andExpect(jsonPath("$[0].additionalServices", is(petForAdminDto.getAdditionalServices())));

        String error = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());
        when(petService.getAllPetsForAdmin(anyLong())).thenThrow(new AccessDeniedException(error));

        mockMvc.perform(get("/pets")
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/pets")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(petService, times(3)).getAllPetsForAdmin(anyLong());
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
