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
import ru.dogudacha.PetHotel.owner.dto.NewOwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerShortDto;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;
import ru.dogudacha.PetHotel.owner.model.MethodsOfCommunication;
import ru.dogudacha.PetHotel.owner.service.OwnerService;
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
    private OwnerService ownerService;


    long ownerId = 2L;
    long requesterId = 1L;
    private final String requesterHeader = "X-PetHotel-User-Id";
    final String ownerName = "new Owner";
    final String email = "newOwner@mail.com";
    final String mainPhone = "+79123456789";
    final String optionalPhone = "8(495)1234567";
    final String instagram = "instagram";
    final String youtube = "youtube";
    final String facebook = "facebook";
    final String tiktok = "tiktok";
    final String twitter = "twitter";
    final String telegram = "telegram";
    final String whatsapp = "whatsapp";
    final String viber = "viber";
    final String vk = "vk";
    MethodsOfCommunication preferCommunication = MethodsOfCommunication.MAIN_PHONE;
    final String carRegistrationNumber = "А100МР777";
    NewOwnerDto newOwnerDto = new NewOwnerDto(ownerName, email, mainPhone, optionalPhone, instagram,
            youtube, facebook, tiktok, twitter, telegram, whatsapp, viber, vk, preferCommunication,
            carRegistrationNumber);
    OwnerDto ownerDto = new OwnerDto(ownerId, ownerName, email, mainPhone, optionalPhone, instagram,
            youtube, facebook, tiktok, twitter, telegram, whatsapp, viber, vk, preferCommunication,
            carRegistrationNumber);

    OwnerShortDto ownerShortDto = new OwnerShortDto(ownerName, null);

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
                .andExpect(jsonPath("$.name", is(ownerDto.getName())))
                .andExpect(jsonPath("$.email", is(ownerDto.getEmail())))
                .andExpect(jsonPath("$.mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.instagram", is(ownerDto.getInstagram())))
                .andExpect(jsonPath("$.youtube", is(ownerDto.getYoutube())))
                .andExpect(jsonPath("$.facebook", is(ownerDto.getFacebook())))
                .andExpect(jsonPath("$.tiktok", is(ownerDto.getTiktok())))
                .andExpect(jsonPath("$.twitter", is(ownerDto.getTwitter())))
                .andExpect(jsonPath("$.telegram", is(ownerDto.getTelegram())))
                .andExpect(jsonPath("$.whatsapp", is(ownerDto.getWhatsapp())))
                .andExpect(jsonPath("$.viber", is(ownerDto.getViber())))
                .andExpect(jsonPath("$.vk", is(ownerDto.getVk())))
                .andExpect(jsonPath("$.preferCommunication",
                        is(ownerDto.getPreferCommunication().toString())))
                .andExpect(jsonPath("$.carRegistrationNumber", is(ownerDto.getCarRegistrationNumber())))
        ;

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
                .andExpect(jsonPath("$.name", is(ownerDto.getName())))
                .andExpect(jsonPath("$.email", is(ownerDto.getEmail())))
                .andExpect(jsonPath("$.mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.instagram", is(ownerDto.getInstagram())))
                .andExpect(jsonPath("$.youtube", is(ownerDto.getYoutube())))
                .andExpect(jsonPath("$.facebook", is(ownerDto.getFacebook())))
                .andExpect(jsonPath("$.tiktok", is(ownerDto.getTiktok())))
                .andExpect(jsonPath("$.twitter", is(ownerDto.getTwitter())))
                .andExpect(jsonPath("$.telegram", is(ownerDto.getTelegram())))
                .andExpect(jsonPath("$.whatsapp", is(ownerDto.getWhatsapp())))
                .andExpect(jsonPath("$.viber", is(ownerDto.getViber())))
                .andExpect(jsonPath("$.vk", is(ownerDto.getVk())))
                .andExpect(jsonPath("$.preferCommunication",
                        is(ownerDto.getPreferCommunication().toString())))
                .andExpect(jsonPath("$.carRegistrationNumber", is(ownerDto.getCarRegistrationNumber())));

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
    void getShortOwnerById() {
        ownerShortDto.setContact(whatsapp);
        when(ownerService.getShortOwnerById(anyLong(), anyLong())).thenReturn(ownerShortDto);

        mockMvc.perform(get("/owners/{id}/short", ownerId)
                        .header(requesterHeader, requesterId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(ownerDto.getName())))
                .andExpect(jsonPath("$.contact", is(ownerDto.getWhatsapp())));

        verify(ownerService).getShortOwnerById(requesterId, ownerId);

        when(ownerService.getShortOwnerById(anyLong(), anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/owners/{id}/short", ownerId)
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
                .andExpect(jsonPath("$.name", is(ownerDto.getName())))
                .andExpect(jsonPath("$.email", is(ownerDto.getEmail())))
                .andExpect(jsonPath("$.mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.instagram", is(ownerDto.getInstagram())))
                .andExpect(jsonPath("$.youtube", is(ownerDto.getYoutube())))
                .andExpect(jsonPath("$.facebook", is(ownerDto.getFacebook())))
                .andExpect(jsonPath("$.tiktok", is(ownerDto.getTiktok())))
                .andExpect(jsonPath("$.twitter", is(ownerDto.getTwitter())))
                .andExpect(jsonPath("$.telegram", is(ownerDto.getTelegram())))
                .andExpect(jsonPath("$.whatsapp", is(ownerDto.getWhatsapp())))
                .andExpect(jsonPath("$.viber", is(ownerDto.getViber())))
                .andExpect(jsonPath("$.vk", is(ownerDto.getVk())))
                .andExpect(jsonPath("$.preferCommunication",
                        is(ownerDto.getPreferCommunication().toString())))
                .andExpect(jsonPath("$.carRegistrationNumber", is(ownerDto.getCarRegistrationNumber())));


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
                .andExpect(jsonPath("$.[0].email", is(ownerDto.getEmail())))
                .andExpect(jsonPath("$.[0].mainPhone", is(ownerDto.getMainPhone())))
                .andExpect(jsonPath("$.[0].optionalPhone", is(ownerDto.getOptionalPhone())))
                .andExpect(jsonPath("$.[0].instagram", is(ownerDto.getInstagram())))
                .andExpect(jsonPath("$.[0].youtube", is(ownerDto.getYoutube())))
                .andExpect(jsonPath("$.[0].facebook", is(ownerDto.getFacebook())))
                .andExpect(jsonPath("$.[0].tiktok", is(ownerDto.getTiktok())))
                .andExpect(jsonPath("$.[0].twitter", is(ownerDto.getTwitter())))
                .andExpect(jsonPath("$.[0].telegram", is(ownerDto.getTelegram())))
                .andExpect(jsonPath("$.[0].whatsapp", is(ownerDto.getWhatsapp())))
                .andExpect(jsonPath("$.[0].viber", is(ownerDto.getViber())))
                .andExpect(jsonPath("$.[0].vk", is(ownerDto.getVk())))
                .andExpect(jsonPath("$.[0].preferCommunication",
                        is(ownerDto.getPreferCommunication().toString())))
                .andExpect(jsonPath("$.[0].carRegistrationNumber", is(ownerDto.getCarRegistrationNumber())));
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