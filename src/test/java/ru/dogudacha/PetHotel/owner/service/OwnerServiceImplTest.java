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
import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;
import ru.dogudacha.PetHotel.owner.dto.mapper.OwnerMapper;
import ru.dogudacha.PetHotel.owner.model.Owner;
import ru.dogudacha.PetHotel.owner.repository.OwnerRepository;
import ru.dogudacha.PetHotel.user.dto.UpdateUserDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;
import ru.dogudacha.PetHotel.user.dto.mapper.UserMapper;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;
import ru.dogudacha.PetHotel.user.service.UserServiceImpl;

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

    @Captor
    private ArgumentCaptor<Owner> ownerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void addOwner_whenAddOwnerByBoss_thenOwnerAdded() {
        Roles requesterRole = Roles.ROLE_BOSS;
        requester.setRole(requesterRole);
        OwnerDto newOwnerDto = new OwnerDto(ownerId, ownerName, email);
        Owner newOwner = new Owner(ownerId, ownerName, email);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(newOwner);
        when(ownerRepository.save(newOwner)).thenReturn(newOwner);
        when(ownerMapper.toOwnerDto(newOwner)).thenReturn(newOwnerDto);

        OwnerDto addedOwnerDto = ownerService.addOwner(requesterId, newOwnerDto);

        assertAll(
                () -> assertEquals(addedOwnerDto, newOwnerDto),
                () -> verify(ownerMapper).toOwner(newOwnerDto),
                () -> verify(ownerRepository).save(newOwner),
                () -> verify(ownerMapper).toOwnerDto(newOwner)
        );
    }

    @Test
    void addOwner_whenAddOwnerByAdmin_thenOwnerAdded() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        OwnerDto newOwnerDto = new OwnerDto(ownerId, ownerName, email);
        Owner newOwner = new Owner(ownerId, ownerName, email);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(newOwner);
        when(ownerRepository.save(newOwner)).thenReturn(newOwner);
        when(ownerMapper.toOwnerDto(newOwner)).thenReturn(newOwnerDto);

        OwnerDto addedOwnerDto = ownerService.addOwner(requesterId, newOwnerDto);

        assertAll(
                () -> assertEquals(addedOwnerDto, newOwnerDto),
                () -> verify(ownerMapper).toOwner(newOwnerDto),
                () -> verify(ownerRepository).save(newOwner),
                () -> verify(ownerMapper).toOwnerDto(newOwner)
        );
    }

    @Test
    void addOwner_whenAddOwnerByUser_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        OwnerDto newOwnerDto = new OwnerDto(ownerId, ownerName, email);
        Owner newOwner = new Owner(ownerId, ownerName, email);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(newOwner);

        assertThrows(AccessDeniedException.class,
                () -> ownerService.addOwner(requesterId, newOwnerDto));
    }

    @Test
    void getOwnerById_whenRequesterIsBoss_thenReturnedOwner() {
        Roles requesterRole = Roles.ROLE_BOSS;
        requester.setRole(requesterRole);
        Owner expectedOwner = new Owner();
        OwnerDto expectedOwnerDto = new OwnerDto();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(expectedOwner));
        when(ownerMapper.toOwnerDto(expectedOwner)).thenReturn(expectedOwnerDto);

        OwnerDto returnedOwnerDto = ownerService.getOwnerById(requesterId, ownerId);

        assertAll(
                () -> assertEquals(expectedOwnerDto, returnedOwnerDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(ownerRepository).findById(ownerId),
                () -> verify(ownerMapper).toOwnerDto(expectedOwner)
        );
    }

    @Test
    void getOwnerById_whenRequesterIsAdmin_thenReturnedOwner() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        Owner expectedOwner = new Owner();
        OwnerDto expectedOwnerDto = new OwnerDto();
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.of(expectedOwner));
        when(ownerMapper.toOwnerDto(expectedOwner)).thenReturn(expectedOwnerDto);

        OwnerDto returnedOwnerDto = ownerService.getOwnerById(requesterId, ownerId);

        assertAll(
                () -> assertEquals(expectedOwnerDto, returnedOwnerDto),
                () -> verify(userRepository).findById(requesterId),
                () -> verify(ownerRepository).findById(ownerId),
                () -> verify(ownerMapper).toOwnerDto(expectedOwner)
        );
    }

    @Test
    void getUserById_whenRequesterNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> ownerService.getOwnerById(requesterId, ownerId));
    }

    @Test
    void getUserById_whenUserNotFound_thenNotFoundException() {
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));
        when(ownerRepository.findById(ownerId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> ownerService.getOwnerById(requesterId, ownerId));
    }

    @Test
    void getUserById_whenRequesterIsUser_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));

        assertThrows(AccessDeniedException.class,
                () -> ownerService.getOwnerById(requesterId, ownerId));
    }

    @Test
    void updateUser_whenRequesterBossFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanId() {
        Roles requesterRole = Roles.ROLE_BOSS;
        requester.setRole(requesterRole);

        UpdateOwnerDto newOwnerDto = UpdateOwnerDto.builder()
                .id(ownerId + 1)
                .name("new" + ownerName)
                .email("new" + email)
                .build();

        Owner oldOwner = Owner.builder()
                .id(ownerId)
                .name(ownerName)
                .email(email)
                .build();

        Owner newOwner = Owner.builder()
                .id(newOwnerDto.getId())
                .name(newOwnerDto.getName())
                .email(newOwnerDto.getEmail())
                .build();

        Owner ownerAfter = Owner.builder()
                .id(oldOwner.getId())
                .name(newOwnerDto.getName())
                .email(newOwner.getEmail())
                .build();

        OwnerDto ownerDtoAfter = OwnerDto.builder()
                .id(ownerAfter.getId())
                .name(ownerAfter.getName())
                .email(ownerAfter.getEmail())
                .build();

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
    void updateUser_whenRequesterAdminFoundAndAllNewFieldsNotNull_thenUpdateAllFieldsThanId() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);

        UpdateOwnerDto newOwnerDto = UpdateOwnerDto.builder()
                .id(ownerId + 1)
                .name("new" + ownerName)
                .email("new" + email)
                .build();

        Owner oldOwner = Owner.builder()
                .id(ownerId)
                .name(ownerName)
                .email(email)
                .build();

        Owner newOwner = Owner.builder()
                .id(newOwnerDto.getId())
                .name(newOwnerDto.getName())
                .email(newOwnerDto.getEmail())
                .build();

        Owner ownerAfter = Owner.builder()
                .id(oldOwner.getId())
                .name(newOwnerDto.getName())
                .email(newOwner.getEmail())
                .build();

        OwnerDto ownerDtoAfter = OwnerDto.builder()
                .id(ownerAfter.getId())
                .name(ownerAfter.getName())
                .email(ownerAfter.getEmail())
                .build();

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

        assertThrows(AccessDeniedException.class,
                () -> ownerService.updateOwner(requesterId, ownerId, new UpdateOwnerDto()));
    }

    @Test
    void updateOwner_whenRequesterUser_thenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_ADMIN;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));

        assertThrows(NotFoundException.class,
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

    @Test
    void getAllOwners_whenAdminGetAllOwners_returnAllOwners() {
        Roles requesterRole = Roles.ROLE_ADMIN;
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

    @Test
    void getAllUsers_whenUserGetAllUsers_ThenAccessDeniedException() {
        Roles requesterRole = Roles.ROLE_USER;
        requester.setRole(requesterRole);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(requester));

        assertThrows(AccessDeniedException.class,
                () -> ownerService.getAllUsers(requesterId));
    }

}
