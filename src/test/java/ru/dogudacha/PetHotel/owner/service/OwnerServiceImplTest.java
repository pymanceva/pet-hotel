package ru.dogudacha.PetHotel.owner.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.owner.dto.NewOwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerShortDto;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;
import ru.dogudacha.PetHotel.owner.dto.mapper.OwnerMapper;
import ru.dogudacha.PetHotel.owner.model.MethodsOfCommunication;
import ru.dogudacha.PetHotel.owner.model.Owner;
import ru.dogudacha.PetHotel.owner.repository.OwnerRepository;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OwnerServiceImplTest {

    @InjectMocks
    private OwnerServiceImpl ownerService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private OwnerMapper ownerMapper;

    final long requesterId = 1L;
    final User requester = User.builder()
            .email("requester@mail.ru")
            .name("requester")
            .id(requesterId)
            .role(Roles.ROLE_BOSS)
            .build();
    long ownerId = 2L;
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

    NewOwnerDto newOwnerDto = new NewOwnerDto(ownerId, ownerName, email, mainPhone, optionalPhone, instagram, youtube,
            facebook, tiktok, twitter, telegram, whatsapp, viber, vk, MethodsOfCommunication.MAIN_PHONE,
            carRegistrationNumber);
    Owner owner = new Owner(ownerId, ownerName, email, mainPhone, optionalPhone, instagram, youtube,
            facebook, tiktok, twitter, telegram, whatsapp, viber, vk, MethodsOfCommunication.MAIN_PHONE,
            carRegistrationNumber);
    OwnerDto ownerDto = new OwnerDto(ownerId, ownerName, email, mainPhone, optionalPhone, instagram, youtube,
            facebook, tiktok, twitter, telegram, whatsapp, viber, vk, MethodsOfCommunication.MAIN_PHONE,
            carRegistrationNumber);

    @Captor
    private ArgumentCaptor<Owner> ownerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void addOwner_whenAddOwnerByBoss_thenOwnerAdded() {
        Roles requesterRole = Roles.ROLE_BOSS;
        requester.setRole(requesterRole);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(owner);
        when(ownerRepository.save(owner)).thenReturn(owner);
        when(ownerMapper.toOwnerDto(owner)).thenReturn(ownerDto);

        OwnerDto addedOwnerDto = ownerService.addOwner(requesterId, newOwnerDto);

        assertAll(
                () -> assertEquals(addedOwnerDto, ownerDto),
                () -> verify(ownerMapper).toOwner(newOwnerDto),
                () -> verify(ownerRepository).save(owner),
                () -> verify(ownerMapper).toOwnerDto(owner)
        );
    }

    @Test
    void addOwner_whenAddOwnerByAdmin_thenOwnerAdded() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(owner);
        when(ownerRepository.save(owner)).thenReturn(owner);
        when(ownerMapper.toOwnerDto(owner)).thenReturn(ownerDto);

        OwnerDto addedOwnerDto = ownerService.addOwner(requesterId, newOwnerDto);

        assertAll(
                () -> assertEquals(addedOwnerDto, ownerDto),
                () -> verify(ownerMapper).toOwner(newOwnerDto),
                () -> verify(ownerRepository).save(owner),
                () -> verify(ownerMapper).toOwnerDto(owner)
        );
    }

    @Test
    void addOwner_whenAddOwnerByUser_thenReturnedOwner() {
        Roles requesterRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(owner);

        assertThrows(AccessDeniedException.class,
                () -> ownerService.addOwner(requesterId, newOwnerDto));
    }

    @Test
    void getOwnerById_whenRequesterIsBoss_thenReturnedOwner() {
        Roles requesterRole = Roles.ROLE_BOSS;
        requester.setRole(requesterRole);
        Owner expectedOwner = owner;
        OwnerShortDto expectedOwnerShortDto = new OwnerShortDto(expectedOwner.getName(), expectedOwner.getMainPhone());
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(expectedOwner));
        when(ownerMapper.toOwnerShortDto(expectedOwner)).thenReturn(expectedOwnerShortDto);

        OwnerShortDto returnedOwnerShortDto = ownerService.getShortOwnerById(requesterId, ownerId);

        assertAll(
                () -> assertEquals(expectedOwnerShortDto, returnedOwnerShortDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(ownerRepository).findById(ownerId),
                () -> verify(ownerMapper).toOwnerShortDto(expectedOwner)
        );
    }

    @Test
    void getOwnerById_whenRequesterIsAdmin_thenReturnedOwner() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        Owner expectedOwner = owner;
        OwnerShortDto expectedOwnerShortDto = new OwnerShortDto(expectedOwner.getName(), expectedOwner.getMainPhone());
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(expectedOwner));
        when(ownerMapper.toOwnerShortDto(expectedOwner)).thenReturn(expectedOwnerShortDto);

        OwnerShortDto returnedOwnerShortDto = ownerService.getShortOwnerById(requesterId, ownerId);

        assertAll(
                () -> assertEquals(expectedOwnerShortDto, returnedOwnerShortDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(ownerRepository).findById(ownerId),
                () -> verify(ownerMapper).toOwnerShortDto(expectedOwner)
        );
    }

    @Test
    void getOwnerById_whenRequesterIsUser_thenReturnedOwner() {
        Roles requesterRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        Owner expectedOwner = owner;
        OwnerShortDto expectedOwnerShortDto = new OwnerShortDto(expectedOwner.getName(), expectedOwner.getMainPhone());
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(expectedOwner));
        when(ownerMapper.toOwnerShortDto(expectedOwner)).thenReturn(expectedOwnerShortDto);

        OwnerShortDto returnedOwnerShortDto = ownerService.getShortOwnerById(requesterId, ownerId);

        assertAll(
                () -> assertEquals(expectedOwnerShortDto, returnedOwnerShortDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(ownerRepository).findById(ownerId),
                () -> verify(ownerMapper).toOwnerShortDto(expectedOwner)
        );
    }

    @Test
    void getUserById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> ownerService.getShortOwnerById(requesterId, ownerId));
    }

    @Test
    void getUserById_whenUserNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> ownerService.getShortOwnerById(requesterId, ownerId));
    }

    @Test
    void getUserById_whenRequesterIsUser_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_FINANCIAL;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));

        assertThrows(AccessDeniedException.class,
                () -> ownerService.getShortOwnerById(requesterId, ownerId));
    }

    @Test
    void updateUser_whenRequesterBossFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanId() {
        Roles requesterRole = Roles.ROLE_BOSS;
        requester.setRole(requesterRole);

        UpdateOwnerDto newOwnerDto = new UpdateOwnerDto(ownerId + 1, "new" + ownerName, "new" + email,
                "new" + mainPhone, "new" + optionalPhone, "new" + instagram,
                "new" + youtube, "new" + facebook, "new" + tiktok, "new" + twitter,
                "new" + telegram, "new" + whatsapp, "new" + viber, "new" + vk,
                MethodsOfCommunication.WHATSAPP, "new" + carRegistrationNumber);

        Owner oldOwner = owner;

        Owner newOwner = new Owner(newOwnerDto.getId(), newOwnerDto.getName(), newOwnerDto.getEmail(),
                newOwnerDto.getMainPhone(), newOwnerDto.getOptionalPhone(), newOwnerDto.getInstagram(),
                newOwnerDto.getYoutube(), newOwnerDto.getFacebook(), newOwnerDto.getTiktok(), newOwnerDto.getTwitter(),
                newOwnerDto.getTelegram(), newOwnerDto.getWhatsapp(), newOwnerDto.getViber(), newOwnerDto.getVk(),
                newOwnerDto.getPreferCommunication(), newOwnerDto.getCarRegistrationNumber());

        Owner ownerAfter = new Owner(
                oldOwner.getId(),
                newOwnerDto.getName(),
                newOwner.getEmail(),
                newOwnerDto.getMainPhone(), newOwnerDto.getOptionalPhone(), newOwnerDto.getInstagram(),
                newOwnerDto.getYoutube(), newOwnerDto.getFacebook(), newOwnerDto.getTiktok(), newOwnerDto.getTwitter(),
                newOwnerDto.getTelegram(), newOwnerDto.getWhatsapp(), newOwnerDto.getViber(), newOwnerDto.getVk(),
                newOwnerDto.getPreferCommunication(), newOwnerDto.getCarRegistrationNumber()
        );

        OwnerDto ownerDtoAfter = new OwnerDto(ownerAfter.getId(), ownerAfter.getName(), ownerAfter.getEmail(),
                ownerAfter.getMainPhone(), ownerAfter.getOptionalPhone(), ownerAfter.getInstagram(),
                ownerAfter.getYoutube(), ownerAfter.getFacebook(), ownerAfter.getTiktok(), ownerAfter.getTwitter(),
                ownerAfter.getTelegram(), ownerAfter.getWhatsapp(), ownerAfter.getViber(), ownerAfter.getVk(),
                ownerAfter.getPreferCommunication(), ownerAfter.getCarRegistrationNumber());

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(oldOwner));
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(newOwner);
        when(ownerRepository.save(newOwner)).thenReturn(ownerAfter);
        when(ownerMapper.toOwnerDto(ownerAfter)).thenReturn(ownerDtoAfter);

        OwnerDto returnedOwnerDto = ownerService.updateOwner(requesterId, ownerId, newOwnerDto);

        verify(ownerRepository).save(ownerArgumentCaptor.capture());
        Owner ownerForSave = ownerArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(ownerDtoAfter, returnedOwnerDto, "entity test failed"),
                () -> assertNotEquals(newOwnerDto.getId(), ownerForSave.getId(),
                        "id field test1 failed"),
                () -> assertEquals(oldOwner.getId(), ownerForSave.getId(),
                        "id field test2 failed"),
                () -> assertEquals(newOwnerDto.getName(), ownerForSave.getName(),
                        "name field test failed"),
                () -> assertEquals(newOwnerDto.getEmail(), ownerForSave.getEmail(),
                        "email field test failed"),
                () -> assertEquals(newOwnerDto.getMainPhone(), ownerForSave.getMainPhone(),
                        "MainPhone field test failed"),
                () -> assertEquals(newOwnerDto.getOptionalPhone(), ownerForSave.getOptionalPhone(),
                        "OptionalPhone field test failed"),
                () -> assertEquals(newOwnerDto.getInstagram(), ownerForSave.getInstagram(),
                        "Instagram field test failed"),
                () -> assertEquals(newOwnerDto.getYoutube(), ownerForSave.getYoutube(),
                        "Youtube field test failed"),
                () -> assertEquals(newOwnerDto.getFacebook(), ownerForSave.getFacebook(),
                        "Facebook field test failed"),
                () -> assertEquals(newOwnerDto.getTiktok(), ownerForSave.getTiktok(),
                        "Tiktok field test failed"),
                () -> assertEquals(newOwnerDto.getTwitter(), ownerForSave.getTwitter(),
                        "Twitter field test failed"),
                () -> assertEquals(newOwnerDto.getTelegram(), ownerForSave.getTelegram(),
                        "Telegram field test failed"),
                () -> assertEquals(newOwnerDto.getWhatsapp(), ownerForSave.getWhatsapp(),
                        "Whatsapp field test failed"),
                () -> assertEquals(newOwnerDto.getViber(), ownerForSave.getViber(),
                        "Viber field test failed"),
                () -> assertEquals(newOwnerDto.getVk(), ownerForSave.getVk(),
                        "Vk field test failed"),
                () -> assertEquals(newOwnerDto.getPreferCommunication(), ownerForSave.getPreferCommunication(),
                        "preferCommunication field test failed"),
                () -> assertEquals(newOwnerDto.getCarRegistrationNumber(), ownerForSave.getCarRegistrationNumber(),
                        "carRegistrationNumber field test failed"),


                () -> verify(userRepository).findById(requesterId),
                () -> verify(ownerRepository).findById(ownerId),
                () -> verify(ownerMapper).toOwner(newOwnerDto),
                () -> verify(ownerMapper).toOwnerDto(ownerAfter)
        );
    }

    @Test
    void updateUser_whenRequesterAdminFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanId() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);

        UpdateOwnerDto newOwnerDto = new UpdateOwnerDto(ownerId + 1, "new" + ownerName, "new" + email,
                "new" + mainPhone, "new" + optionalPhone, "new" + instagram,
                "new" + youtube, "new" + facebook, "new" + tiktok, "new" + twitter,
                "new" + telegram, "new" + whatsapp, "new" + viber, "new" + vk,
                MethodsOfCommunication.WHATSAPP, "new" + carRegistrationNumber);

        Owner oldOwner = owner;

        Owner newOwner = new Owner(newOwnerDto.getId(), newOwnerDto.getName(), newOwnerDto.getEmail(),
                newOwnerDto.getMainPhone(), newOwnerDto.getOptionalPhone(), newOwnerDto.getInstagram(),
                newOwnerDto.getYoutube(), newOwnerDto.getFacebook(), newOwnerDto.getTiktok(), newOwnerDto.getTwitter(),
                newOwnerDto.getTelegram(), newOwnerDto.getWhatsapp(), newOwnerDto.getViber(), newOwnerDto.getVk(),
                newOwnerDto.getPreferCommunication(), newOwnerDto.getCarRegistrationNumber());

        Owner ownerAfter = new Owner(
                oldOwner.getId(),
                newOwnerDto.getName(),
                newOwner.getEmail(),
                newOwnerDto.getMainPhone(), newOwnerDto.getOptionalPhone(), newOwnerDto.getInstagram(),
                newOwnerDto.getYoutube(), newOwnerDto.getFacebook(), newOwnerDto.getTiktok(), newOwnerDto.getTwitter(),
                newOwnerDto.getTelegram(), newOwnerDto.getWhatsapp(), newOwnerDto.getViber(), newOwnerDto.getVk(),
                newOwnerDto.getPreferCommunication(), newOwnerDto.getCarRegistrationNumber());

        OwnerDto ownerDtoAfter = new OwnerDto(ownerAfter.getId(), ownerAfter.getName(), ownerAfter.getEmail(),
                ownerAfter.getMainPhone(), ownerAfter.getOptionalPhone(), ownerAfter.getInstagram(),
                ownerAfter.getYoutube(), ownerAfter.getFacebook(), ownerAfter.getTiktok(), ownerAfter.getTwitter(),
                ownerAfter.getTelegram(), ownerAfter.getWhatsapp(), ownerAfter.getViber(), ownerAfter.getVk(),
                ownerAfter.getPreferCommunication(), ownerAfter.getCarRegistrationNumber());

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(oldOwner));
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(newOwner);
        when(ownerRepository.save(newOwner)).thenReturn(ownerAfter);
        when(ownerMapper.toOwnerDto(ownerAfter)).thenReturn(ownerDtoAfter);

        OwnerDto returnedOwnerDto = ownerService.updateOwner(requesterId, ownerId, newOwnerDto);

        verify(ownerRepository).save(ownerArgumentCaptor.capture());
        Owner ownerForSave = ownerArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(ownerDtoAfter, returnedOwnerDto, "entity field test failed"),
                () -> assertNotEquals(newOwnerDto.getId(), ownerForSave.getId(), "id field test1 failed"),
                () -> assertEquals(oldOwner.getId(), ownerForSave.getId(), "id field test2 failed"),
                () -> assertEquals(newOwnerDto.getName(), ownerForSave.getName(), "name field test failed"),
                () -> assertEquals(newOwnerDto.getEmail(), ownerForSave.getEmail(), "email field test failed"),

                () -> verify(userRepository).findById(requesterId),
                () -> verify(ownerRepository).findById(ownerId),
                () -> verify(ownerMapper).toOwner(newOwnerDto),
                () -> verify(ownerMapper).toOwnerDto(ownerAfter)
        );
    }

    @Test
    void updateOwner_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> ownerService.updateOwner(requesterId, ownerId, new UpdateOwnerDto()));
    }

    @Test
    void updateUser_whenOwnerNotFound_thenNotFoundException() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> ownerService.updateOwner(requesterId, ownerId, new UpdateOwnerDto()));
    }

    @Test
    void updateOwner_whenRequesterUser_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));

        assertThrows(AccessDeniedException.class,
                () -> ownerService.updateOwner(requesterId, ownerId, new UpdateOwnerDto()));
    }

    @Test
    void getAllOwners_whenBossGetAllOwners_returnAllOwners() {
        Roles requesterRole = Roles.ROLE_BOSS;
        List<Roles> roles = Arrays.stream(Roles.values()).toList();
        requester.setRole(requesterRole);

        Owner owner1 = new Owner(ownerId + 1, "1" + ownerName, "1" + email);
        Owner owner2 = new Owner(ownerId + 2, "2" + ownerName, "2" + email);
        Owner owner3 = new Owner(ownerId + 3, "3" + ownerName, "3" + email);
        List<Owner> ownerList = List.of(owner1, owner2, owner3);
        OwnerDto ownerDto1 = new OwnerDto(ownerId + 1, "1" + ownerName, "1" + email);
        OwnerDto ownerDto2 = new OwnerDto(ownerId + 2, "2" + ownerName, "2" + email);
        OwnerDto ownerDto3 = new OwnerDto(ownerId + 3, "3" + ownerName, "3" + email);
        List<OwnerDto> ownerDtoList = List.of(ownerDto1, ownerDto2, ownerDto3);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findAll()).thenReturn(ownerList);
        when(ownerMapper.map(ownerList)).thenReturn(ownerDtoList);

        List<OwnerDto> returnedOwnersDto = ownerService.getAllOwners(requesterId);

        assertAll(
                () -> assertEquals(ownerDtoList, returnedOwnersDto),
                () -> verify(ownerRepository).findAll(),
                () -> verify(ownerMapper).map(ownerList)
        );
    }

//    @Test
//    void getAllOwners_whenAdminGetAllOwners_returnAllOwners() {
//        Roles requesterRole = Roles.ROLE_ADMIN;
//        List<Roles> roles = Arrays.stream(Roles.values()).toList();
//        requester.setRole(requesterRole);
//
//        Owner owner1 = new Owner(ownerId + 1, "1" + ownerName, "1" + email);
//        Owner owner2 = new Owner(ownerId + 2, "2" + ownerName, "2" + email);
//        Owner owner3 = new Owner(ownerId + 3, "3" + ownerName, "3" + email);
//        List<Owner> ownerList = List.of(owner1, owner2, owner3);
//        OwnerDto ownerDto1 = new OwnerDto(ownerId + 1, "1" + ownerName, "1" + email);
//        OwnerDto ownerDto2 = new OwnerDto(ownerId + 2, "2" + ownerName, "2" + email);
//        OwnerDto ownerDto3 = new OwnerDto(ownerId + 3, "3" + ownerName, "3" + email);
//        List<OwnerDto> ownerDtoList = List.of(ownerDto1, ownerDto2, ownerDto3);
//
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//        when(ownerRepository.findAll()).thenReturn(ownerList);
//        when(ownerMapper.map(ownerList)).thenReturn(ownerDtoList);
//
//        List<OwnerDto> returnedOwnersDto = ownerService.getAllOwners(requesterId);
//
//        assertAll(
//                () -> assertEquals(ownerDtoList, returnedOwnersDto),
//                () -> verify(ownerRepository).findAll(),
//                () -> verify(ownerMapper).map(ownerList)
//        );
//    }
//
//    @Test
//    void getAllUsers_whenUserGetAllUsers_ThenAccessDeniedException() {
//        Roles requesterRole = Roles.ROLE_USER;
//        requester.setRole(requesterRole);
//        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
//
//        assertThrows(AccessDeniedException.class,
//                () -> ownerService.getAllUsers(requesterId));
//    }

}
