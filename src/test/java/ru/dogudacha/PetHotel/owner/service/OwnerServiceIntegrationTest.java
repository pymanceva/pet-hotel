package ru.dogudacha.PetHotel.owner.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.owner.dto.NewOwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerShortDto;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;
import ru.dogudacha.PetHotel.owner.model.MethodsOfCommunication;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OwnerServiceIntegrationTest {

    private final EntityManager em;
    private final OwnerService ownerService;

    final User requester = User.builder()
            .email("requester@mail.ru")
            .name("requester")
            .role(Roles.ROLE_BOSS)
            .build();
    long requesterId;
    long ownerId;
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
    final String carRegistrationNumber = "А100МР777";

    NewOwnerDto newOwnerDto1 = new NewOwnerDto(ownerName + "1", email + "1", mainPhone + "1",
            optionalPhone + "1", instagram + "1", youtube + "1", facebook + "1",
            tiktok + "1", twitter + "1", telegram + "1", whatsapp + "1",
            viber + "1", vk + "1", MethodsOfCommunication.MAIN_PHONE,
            carRegistrationNumber + "1");
    NewOwnerDto newOwnerDto2 = new NewOwnerDto(ownerName + "2", email + "2", mainPhone + "2",
            optionalPhone + "2", instagram + "2", youtube + "2", facebook + "2",
            tiktok + "2", twitter + "2", telegram + "2", whatsapp + "2",
            viber + "2", vk + "2", MethodsOfCommunication.OPTIONAL_PHONE,
            carRegistrationNumber + "2");
    NewOwnerDto newOwnerDto3 = new NewOwnerDto(ownerName + "3", email + "3", mainPhone + "3",
            optionalPhone + "3", instagram + "3", youtube + "3", facebook + "3",
            tiktok + "3", twitter + "3", telegram + "3", whatsapp + "3",
            viber + "3", vk + "3", MethodsOfCommunication.MAIN_PHONE,
            carRegistrationNumber + "3");
    OwnerDto addedOwnerDto1;
    OwnerDto addedOwnerDto2;
    OwnerDto addedOwnerDto3;

    @Test
    @Order(1)
    void context() {
        assertAll(
                () -> assertNotNull(ownerService)
        );
    }

    @Test
    @Order(2)
    void addOwner() {
        em.persist(requester);
        requesterId = requester.getId();
        addedOwnerDto1 = ownerService.addOwner(requesterId, newOwnerDto1);

        ownerId = addedOwnerDto1.getId();

        assertAll(
                () -> assertNotNull(addedOwnerDto1),
                () -> assertNotNull(addedOwnerDto1.getId(),
                        "id field test failed"),
                () -> assertEquals(newOwnerDto1.getName(), addedOwnerDto1.getName(),
                        "name field test failed"),
                () -> assertEquals(newOwnerDto1.getEmail(), addedOwnerDto1.getEmail(),
                        "email field test failed"),
                () -> assertEquals(newOwnerDto1.getMainPhone(), addedOwnerDto1.getMainPhone(),
                        "MainPhone field test failed"),
                () -> assertEquals(newOwnerDto1.getOptionalPhone(), addedOwnerDto1.getOptionalPhone(),
                        "OptionalPhone field test failed"),
                () -> assertEquals(newOwnerDto1.getInstagram(), addedOwnerDto1.getInstagram(),
                        "Instagram field test failed"),
                () -> assertEquals(newOwnerDto1.getYoutube(), addedOwnerDto1.getYoutube(),
                        "Youtube field test failed"),
                () -> assertEquals(newOwnerDto1.getFacebook(), addedOwnerDto1.getFacebook(),
                        "Facebook field test failed"),
                () -> assertEquals(newOwnerDto1.getTiktok(), addedOwnerDto1.getTiktok(),
                        "Tiktok field test failed"),
                () -> assertEquals(newOwnerDto1.getTwitter(), addedOwnerDto1.getTwitter(),
                        "Twitter field test failed"),
                () -> assertEquals(newOwnerDto1.getTelegram(), addedOwnerDto1.getTelegram(),
                        "Telegram field test failed"),
                () -> assertEquals(newOwnerDto1.getWhatsapp(), addedOwnerDto1.getWhatsapp(),
                        "Whatsapp field test failed"),
                () -> assertEquals(newOwnerDto1.getViber(), addedOwnerDto1.getViber(),
                        "Viber field test failed"),
                () -> assertEquals(newOwnerDto1.getVk(), addedOwnerDto1.getVk(),
                        "Vk field test failed"),
                () -> assertEquals(newOwnerDto1.getPreferCommunication(), addedOwnerDto1.getPreferCommunication(),
                        "preferCommunication field test failed"),
                () -> assertEquals(newOwnerDto1.getCarRegistrationNumber(), addedOwnerDto1.getCarRegistrationNumber(),
                        "carRegistrationNumber field test failed")
        );

        OwnerDto ownerById = ownerService.getOwnerById(requesterId, ownerId);

        assertAll(
                () -> assertNotNull(ownerById),
                () -> assertNotNull(ownerById.getId(),
                        "id field test failed"),
                () -> assertEquals(ownerById.getName(), addedOwnerDto1.getName(),
                        "name field test failed"),
                () -> assertEquals(ownerById.getEmail(), addedOwnerDto1.getEmail(),
                        "email field test failed"),
                () -> assertEquals(ownerById.getMainPhone(), addedOwnerDto1.getMainPhone(),
                        "MainPhone field test failed"),
                () -> assertEquals(ownerById.getOptionalPhone(), addedOwnerDto1.getOptionalPhone(),
                        "OptionalPhone field test failed"),
                () -> assertEquals(ownerById.getInstagram(), addedOwnerDto1.getInstagram(),
                        "Instagram field test failed"),
                () -> assertEquals(ownerById.getYoutube(), addedOwnerDto1.getYoutube(),
                        "Youtube field test failed"),
                () -> assertEquals(ownerById.getFacebook(), addedOwnerDto1.getFacebook(),
                        "Facebook field test failed"),
                () -> assertEquals(ownerById.getTiktok(), addedOwnerDto1.getTiktok(),
                        "Tiktok field test failed"),
                () -> assertEquals(ownerById.getTwitter(), addedOwnerDto1.getTwitter(),
                        "Twitter field test failed"),
                () -> assertEquals(ownerById.getTelegram(), addedOwnerDto1.getTelegram(),
                        "Telegram field test failed"),
                () -> assertEquals(ownerById.getWhatsapp(), addedOwnerDto1.getWhatsapp(),
                        "Whatsapp field test failed"),
                () -> assertEquals(ownerById.getViber(), addedOwnerDto1.getViber(),
                        "Viber field test failed"),
                () -> assertEquals(ownerById.getVk(), addedOwnerDto1.getVk(),
                        "Vk field test failed"),
                () -> assertEquals(ownerById.getPreferCommunication(), addedOwnerDto1.getPreferCommunication(),
                        "preferCommunication field test failed"),
                () -> assertEquals(ownerById.getCarRegistrationNumber(), addedOwnerDto1.getCarRegistrationNumber(),
                        "carRegistrationNumber field test failed"),
                () -> assertNotSame(ownerById, addedOwnerDto1)
        );

        OwnerShortDto shortOwnerById = ownerService.getShortOwnerById(requester.getId(), ownerId);

        assertAll(
                () -> assertNotNull(shortOwnerById),
                () -> assertEquals(shortOwnerById.getName(), addedOwnerDto1.getName(),
                        "name field test failed"),
                () -> assertEquals(shortOwnerById.getContact(), addedOwnerDto1.getMainPhone(),
                        "contact field test failed")
        );

        MethodsOfCommunication updMethodOfCommunication = MethodsOfCommunication.WHATSAPP;
        String updWhatsapp = "updWhatsapp";

        UpdateOwnerDto updateOwnerDto = new UpdateOwnerDto(null, null, null, null,
                null, null, null, null, null,
                null, updWhatsapp, null, null, updMethodOfCommunication, null);

        OwnerDto updateOwner = ownerService.updateOwner(requester.getId(), ownerId, updateOwnerDto);

        assertAll(
                () -> assertNotNull(updateOwner),
                () -> assertNotNull(updateOwner.getId(),
                        "id field test failed"),
                () -> assertEquals(updateOwner.getName(), addedOwnerDto1.getName(),
                        "name field test failed"),
                () -> assertEquals(updateOwner.getEmail(), addedOwnerDto1.getEmail(),
                        "email field test failed"),
                () -> assertEquals(updateOwner.getMainPhone(), addedOwnerDto1.getMainPhone(),
                        "MainPhone field test failed"),
                () -> assertEquals(updateOwner.getOptionalPhone(), addedOwnerDto1.getOptionalPhone(),
                        "OptionalPhone field test failed"),
                () -> assertEquals(updateOwner.getInstagram(), addedOwnerDto1.getInstagram(),
                        "Instagram field test failed"),
                () -> assertEquals(updateOwner.getYoutube(), addedOwnerDto1.getYoutube(),
                        "Youtube field test failed"),
                () -> assertEquals(updateOwner.getFacebook(), addedOwnerDto1.getFacebook(),
                        "Facebook field test failed"),
                () -> assertEquals(updateOwner.getTiktok(), addedOwnerDto1.getTiktok(),
                        "Tiktok field test failed"),
                () -> assertEquals(updateOwner.getTwitter(), addedOwnerDto1.getTwitter(),
                        "Twitter field test failed"),
                () -> assertEquals(updateOwner.getTelegram(), addedOwnerDto1.getTelegram(),
                        "Telegram field test failed"),
                () -> assertEquals(updateOwner.getWhatsapp(), updWhatsapp,
                        "Whatsapp field test failed"),
                () -> assertEquals(updateOwner.getViber(), addedOwnerDto1.getViber(),
                        "Viber field test failed"),
                () -> assertEquals(updateOwner.getVk(), addedOwnerDto1.getVk(),
                        "Vk field test failed"),
                () -> assertEquals(updateOwner.getPreferCommunication(), updMethodOfCommunication,
                        "preferCommunication field test failed"),
                () -> assertEquals(updateOwner.getCarRegistrationNumber(), addedOwnerDto1.getCarRegistrationNumber(),
                        "carRegistrationNumber field test failed"),
                () -> assertNotSame(updateOwner, addedOwnerDto1)
        );

        OwnerShortDto shortOwnerByIdAfterUpd = ownerService.getShortOwnerById(requester.getId(), ownerId);

        assertAll(
                () -> assertNotNull(shortOwnerByIdAfterUpd),
                () -> assertEquals(shortOwnerByIdAfterUpd.getName(), addedOwnerDto1.getName(),
                        "name field test failed"),
                () -> assertEquals(shortOwnerByIdAfterUpd.getContact(), updWhatsapp,
                        "contact field test failed")
        );

        addedOwnerDto2 = ownerService.addOwner(requesterId, newOwnerDto2);
        addedOwnerDto3 = ownerService.addOwner(requesterId, newOwnerDto3);
        Collection<OwnerDto> allOwners = ownerService.getAllOwners(requesterId);

        assertAll(
                () -> assertNotNull(allOwners),
                () -> assertEquals(3, allOwners.size()),
                () -> assertThat(allOwners, hasItem(updateOwner)),
                () -> assertThat(allOwners, hasItem(addedOwnerDto2)),
                () -> assertThat(allOwners, hasItem(addedOwnerDto3))
        );

        ownerService.deleteOwnerById(requesterId, addedOwnerDto2.getId());

        Collection<OwnerDto> allOwnersAfterDelete = ownerService.getAllOwners(requesterId);

        assertAll(
                () -> assertNotNull(allOwnersAfterDelete),
                () -> assertEquals(2, allOwnersAfterDelete.size()),
                () -> assertThat(allOwnersAfterDelete, hasItem(updateOwner)),
                () -> assertThat(allOwnersAfterDelete, not(hasItem(addedOwnerDto2))),
                () -> assertThat(allOwnersAfterDelete, hasItem(addedOwnerDto3))
        );
    }
}

