package ru.modgy.owner;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.modgy.exception.NotFoundException;
import ru.modgy.owner.controller.SearchDirection;
import ru.modgy.owner.controller.OwnerController;
import ru.modgy.owner.dto.*;
import ru.modgy.owner.service.OwnerService;
import ru.modgy.utility.UtilityService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.now;
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
    private OwnerService ownerService;
    @MockBean
    private UtilityService utilityService;

    long ownerId = 2L;
    long requesterId = 1L;
    private final String requesterHeader = UtilityService.REQUESTER_ID_HEADER;
    final String ownerLastName = "new OwnerLast";
    final String ownerFirstName = "new OwnerFirst";
    final String ownerMiddleName = "new OwnerMiddle";
    final String mainPhone = "+79123456789";
    final String optionalPhone = "84951234567";
    final String otherContacts = "instagram";
    final String actualAddress = "г. Воронеж, ул. Лизюкова, 16";
    final String trustedMan = "Деверя золовки сват, 8(800)555-35-35";
    final String source = "По радио передавали";
    final String comment = "Норм парень";
    final int rating = 1;
    final LocalDateTime registrationDate = now().truncatedTo(ChronoUnit.SECONDS);

    NewOwnerDto newOwnerDto = new NewOwnerDto(ownerLastName, ownerFirstName, ownerMiddleName, mainPhone, optionalPhone,
            otherContacts, actualAddress, trustedMan, source, comment, rating);
    OwnerDto ownerDto = new OwnerDto(ownerId, ownerLastName, ownerFirstName, ownerMiddleName, mainPhone, optionalPhone,
            otherContacts, actualAddress, trustedMan, source, comment, rating, registrationDate);

    OwnerShortDto ownerShortDto = new OwnerShortDto(ownerId, ownerLastName, ownerFirstName, ownerMiddleName, mainPhone,
            optionalPhone, registrationDate);

    @Test
    @SneakyThrows
    void addOwner() {
        when(ownerService.addOwner(anyLong(), any(NewOwnerDto.class))).thenReturn(ownerDto);

        mockMvc.perform(post("/owners")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newOwnerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.otherContacts", is(ownerDto.getOtherContacts())))
                .andExpect(jsonPath("$.actualAddress", is(ownerDto.getActualAddress())))
                .andExpect(jsonPath("$.trustedMan", is(ownerDto.getTrustedMan())))
                .andExpect(jsonPath("$.source", is(ownerDto.getSource())))
                .andExpect(jsonPath("$.comment", is(ownerDto.getComment())))
                .andExpect(jsonPath("$.rating", is(ownerDto.getRating())))
                .andExpect(jsonPath("$.registrationDate").value(ownerDto.getRegistrationDate().toString()));

        verify(ownerService).addOwner(anyLong(), any(NewOwnerDto.class));

        mockMvc.perform(post("/owners")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new NewOwnerDto())))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/owners")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new NewOwnerDto())))
                .andExpect(status().isBadRequest());

        verify(ownerService, times(1)).addOwner(anyLong(), any(NewOwnerDto.class));
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
                .andExpect(jsonPath("$.lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.otherContacts", is(ownerDto.getOtherContacts())))
                .andExpect(jsonPath("$.actualAddress", is(ownerDto.getActualAddress())))
                .andExpect(jsonPath("$.trustedMan", is(ownerDto.getTrustedMan())))
                .andExpect(jsonPath("$.source", is(ownerDto.getSource())))
                .andExpect(jsonPath("$.comment", is(ownerDto.getComment())))
                .andExpect(jsonPath("$.rating", is(ownerDto.getRating())))
                .andExpect(jsonPath("$.registrationDate").value(ownerDto.getRegistrationDate().toString()));

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
    void getSomeShortOwners_whenNumIsAbsent_ThenNumIs10() {
        when(ownerService.getSomeShortOwners(anyLong(), anyInt())).thenReturn(List.of(ownerShortDto));
        mockMvc.perform(get("/owners/short")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.[0].firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.[0].middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.[0].mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.[0].optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.[0].registrationDate")
                        .value(ownerDto.getRegistrationDate().toString()));

        verify(ownerService).getSomeShortOwners(requesterId, 10);
    }

    @Test
    @SneakyThrows
    void getSomeShortOwners_whenNumIsPositive_ThenOk() {
        int num = 5;
        String numParam = String.valueOf(num);
        when(ownerService.getSomeShortOwners(anyLong(), anyInt())).thenReturn(List.of(ownerShortDto));

        mockMvc.perform(get("/owners/short")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("num", numParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.[0].firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.[0].middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.[0].mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.[0].optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.[0].registrationDate")
                        .value(ownerDto.getRegistrationDate().toString()));

        verify(ownerService).getSomeShortOwners(requesterId, num);
    }

    @Test
    @SneakyThrows
    void getSomeShortOwners_whenNumIsNegativeOrZero_ThenBadRequest() {
        int num = -5;
        String numParam = String.valueOf(num);
        when(ownerService.getSomeShortOwners(anyLong(), anyInt())).thenReturn(List.of(ownerShortDto));

        mockMvc.perform(get("/owners/short")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("num", numParam))
                .andExpect(status().isBadRequest());

        num = 0;
        numParam = String.valueOf(num);
        when(ownerService.getSomeShortOwners(anyLong(), anyInt())).thenReturn(List.of(ownerShortDto));

        mockMvc.perform(get("/owners/short")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("num", numParam))
                .andExpect(status().isBadRequest());

        verify(ownerService, never()).getSomeShortOwners(anyLong(), anyInt());
    }

    @Test
    @SneakyThrows
    void getSomeShortOwners_whenRequesterHeaderIsAbsent_ThenBadRequest() {
        mockMvc.perform(post("/owners")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new NewOwnerDto())))
                .andExpect(status().isBadRequest());

        verify(ownerService, never()).getSomeShortOwners(anyLong(), anyInt());
    }


    @Test
    @SneakyThrows
    void getShortOwnerById() {
        when(ownerService.getShortOwnerById(anyLong(), anyLong())).thenReturn(ownerShortDto);

        mockMvc.perform(get("/owners/short/{id}", ownerId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.registrationDate").value(ownerDto.getRegistrationDate().toString()));

        verify(ownerService).getShortOwnerById(requesterId, ownerId);

        when(ownerService.getShortOwnerById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/owners/short/{id}", ownerId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(ownerService, times(2)).getShortOwnerById(requesterId, ownerId);
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
                .andExpect(jsonPath("$.lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.otherContacts", is(ownerDto.getOtherContacts())))
                .andExpect(jsonPath("$.actualAddress", is(ownerDto.getActualAddress())))
                .andExpect(jsonPath("$.trustedMan", is(ownerDto.getTrustedMan())))
                .andExpect(jsonPath("$.source", is(ownerDto.getSource())))
                .andExpect(jsonPath("$.comment", is(ownerDto.getComment())))
                .andExpect(jsonPath("$.rating", is(ownerDto.getRating())))
                .andExpect(jsonPath("$.registrationDate").value(ownerDto.getRegistrationDate().toString()));


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
                .andExpect(jsonPath("$.[0].lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.[0].firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.[0].middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.[0].mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.[0].optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.[0].otherContacts", is(ownerDto.getOtherContacts())))
                .andExpect(jsonPath("$.[0].actualAddress", is(ownerDto.getActualAddress())))
                .andExpect(jsonPath("$.[0].trustedMan", is(ownerDto.getTrustedMan())))
                .andExpect(jsonPath("$.[0].source", is(ownerDto.getSource())))
                .andExpect(jsonPath("$.[0].comment", is(ownerDto.getComment())))
                .andExpect(jsonPath("$.[0].rating", is(ownerDto.getRating())))
                .andExpect(jsonPath("$.[0].registrationDate")
                        .value(ownerDto.getRegistrationDate().toString()));
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

    @Test
    @SneakyThrows
    void checkOwnerPhoneNumber_WhenOwnerAlreadyExists_ThenReturnExistOwner() {
        CheckOwnerDto checkOwnerDto = new CheckOwnerDto("81234567890");
        when(ownerService.checkOwnerPhoneNumber(anyLong(), eq(checkOwnerDto))).thenReturn(ownerDto);

        mockMvc.perform(post("/owners/check")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(checkOwnerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.otherContacts", is(ownerDto.getOtherContacts())))
                .andExpect(jsonPath("$.actualAddress", is(ownerDto.getActualAddress())))
                .andExpect(jsonPath("$.trustedMan", is(ownerDto.getTrustedMan())))
                .andExpect(jsonPath("$.source", is(ownerDto.getSource())))
                .andExpect(jsonPath("$.comment", is(ownerDto.getComment())))
                .andExpect(jsonPath("$.rating", is(ownerDto.getRating())))
                .andExpect(jsonPath("$.registrationDate").value(ownerDto.getRegistrationDate().toString()));

        verify(ownerService, times(1)).checkOwnerPhoneNumber(requesterId, checkOwnerDto);
    }

    @Test
    @SneakyThrows
    void checkOwnerPhoneNumber_WhenOwnerNotExists_ThenReturnEmptyOwner() {
        CheckOwnerDto checkOwnerDto = new CheckOwnerDto("81234567890");
        when(ownerService.checkOwnerPhoneNumber(anyLong(), eq(checkOwnerDto))).thenReturn(new OwnerDto());

        mockMvc.perform(post("/owners/check")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.ALL_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(checkOwnerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.lastName").isEmpty())
                .andExpect(jsonPath("$.firstName").isEmpty())
                .andExpect(jsonPath("$.middleName").isEmpty())
                .andExpect(jsonPath("$.mainPhone").isEmpty())
                .andExpect(jsonPath("$.optionalPhone").isEmpty())
                .andExpect(jsonPath("$.otherContacts").isEmpty())
                .andExpect(jsonPath("$.actualAddress").isEmpty())
                .andExpect(jsonPath("$.trustedMan").isEmpty())
                .andExpect(jsonPath("$.source").isEmpty())
                .andExpect(jsonPath("$.comment").isEmpty())
                .andExpect(jsonPath("$.rating").isEmpty())
                .andExpect(jsonPath("$.registrationDate").isEmpty());

        verify(ownerService, times(1)).checkOwnerPhoneNumber(requesterId, checkOwnerDto);
    }

    @Test
    @SneakyThrows
    void checkOwnerPhoneNumber_whenRequesterHeaderIsAbsent_ThenBadRequest() {
        mockMvc.perform(post("/owners/check")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new CheckOwnerDto())))
                .andExpect(status().isBadRequest());

        verify(ownerService, never()).checkOwnerPhoneNumber(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void checkOwnerPhoneNumber_whenRequestBodyIsNotValid_ThenBadRequest() {
        mockMvc.perform(post("/owners/check")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new CheckOwnerDto())))
                .andExpect(status().isBadRequest());

        verify(ownerService, never()).checkOwnerPhoneNumber(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void searchOwner_whenAllDataOk_thenOk() {
        SearchOwnerDto searchOwnerDto = new SearchOwnerDto("123");
        SearchDirection searchDirection = SearchDirection.PHONE;
        when(ownerService.searchOwner(requesterId, searchOwnerDto, searchDirection)).thenReturn(List.of(ownerDto));

        mockMvc.perform(post("/owners/search")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(searchOwnerDto))
                        .param("direction", searchDirection.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.[0].firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.[0].middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.[0].mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.[0].optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.[0].otherContacts", is(ownerDto.getOtherContacts())))
                .andExpect(jsonPath("$.[0].actualAddress", is(ownerDto.getActualAddress())))
                .andExpect(jsonPath("$.[0].trustedMan", is(ownerDto.getTrustedMan())))
                .andExpect(jsonPath("$.[0].source", is(ownerDto.getSource())))
                .andExpect(jsonPath("$.[0].comment", is(ownerDto.getComment())))
                .andExpect(jsonPath("$.[0].rating", is(ownerDto.getRating())))
                .andExpect(jsonPath("$.[0].registrationDate")
                        .value(ownerDto.getRegistrationDate().toString()));

        verify(ownerService).searchOwner(requesterId, searchOwnerDto, searchDirection);

        searchDirection = SearchDirection.NAME;
        when(ownerService.searchOwner(requesterId, searchOwnerDto, searchDirection)).thenReturn(List.of(ownerDto));

        mockMvc.perform(post("/owners/search")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(searchOwnerDto))
                        .param("direction", searchDirection.getTitle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(ownerDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].lastName", is(ownerDto.getLastName())))
                .andExpect(jsonPath("$.[0].firstName", is(ownerDto.getFirstName())))
                .andExpect(jsonPath("$.[0].middleName", is(ownerDto.getMiddleName())))
                .andExpect(jsonPath("$.[0].mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.[0].optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.[0].otherContacts", is(ownerDto.getOtherContacts())))
                .andExpect(jsonPath("$.[0].actualAddress", is(ownerDto.getActualAddress())))
                .andExpect(jsonPath("$.[0].trustedMan", is(ownerDto.getTrustedMan())))
                .andExpect(jsonPath("$.[0].source", is(ownerDto.getSource())))
                .andExpect(jsonPath("$.[0].comment", is(ownerDto.getComment())))
                .andExpect(jsonPath("$.[0].rating", is(ownerDto.getRating())))
                .andExpect(jsonPath("$.[0].registrationDate")
                        .value(ownerDto.getRegistrationDate().toString()));

        verify(ownerService).searchOwner(requesterId, searchOwnerDto, searchDirection);
    }

    @Test
    @SneakyThrows
    void searchOwner_whenWithoutBody_then4xx() {
        SearchOwnerDto searchOwnerDto = new SearchOwnerDto("123");
        SearchDirection searchDirection = SearchDirection.PHONE;
        when(ownerService.searchOwner(requesterId, searchOwnerDto, searchDirection)).thenReturn(List.of(ownerDto));

        mockMvc.perform(post("/owners/search")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("direction", searchDirection.getTitle()))
                .andExpect(status().is4xxClientError());

        verify(ownerService, never()).searchOwner(requesterId, searchOwnerDto, searchDirection);
    }

    @Test
    @SneakyThrows
    void searchOwner_whenWithoutDirectionParam_then4xx() {
        SearchOwnerDto searchOwnerDto = new SearchOwnerDto("123");
        SearchDirection searchDirection = SearchDirection.PHONE;
        when(ownerService.searchOwner(requesterId, searchOwnerDto, searchDirection)).thenReturn(List.of(ownerDto));

        mockMvc.perform(post("/owners/search")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(searchOwnerDto)))
                .andExpect(status().is4xxClientError());

        verify(ownerService, never()).searchOwner(requesterId, searchOwnerDto, searchDirection);
    }


    @Test
    @SneakyThrows
    void searchOwner_whenDirectionParamIsWrong_then4xx() {
        SearchOwnerDto searchOwnerDto = new SearchOwnerDto("123");
        when(ownerService.searchOwner(eq(requesterId), eq(searchOwnerDto), any())).thenReturn(List.of(ownerDto));

        mockMvc.perform(post("/owners/search")
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(searchOwnerDto))
                        .param("direction", "bla-bla"))
                .andExpect(status().is4xxClientError());

        verify(ownerService, never()).searchOwner(eq(requesterId), eq(searchOwnerDto), any());
    }
}