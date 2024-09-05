package ru.modgy.owner.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;
import ru.modgy.owner.controller.SearchDirection;
import ru.modgy.owner.dto.*;
import ru.modgy.owner.dto.mapper.OwnerMapper;
import ru.modgy.owner.model.Owner;
import ru.modgy.owner.repository.OwnerRepository;
import ru.modgy.pet.dto.PetDtoForOwner;
import ru.modgy.pet.model.Pet;
import ru.modgy.utility.EntityService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OwnerServiceImplTest {
    @InjectMocks
    private OwnerServiceImpl ownerService;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private OwnerMapper ownerMapper;
    @Mock
    private EntityService entityService;

    final long requesterId = 1L;
    long ownerId = 2L;
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
    final List<Pet> pets = List.of();
    final List<PetDtoForOwner> petsDto = List.of();

    Owner owner = new Owner(ownerId, ownerLastName, ownerFirstName, ownerMiddleName, mainPhone, optionalPhone,
            otherContacts, actualAddress, trustedMan, source, comment, rating, registrationDate, pets);
    NewOwnerDto newOwnerDto = new NewOwnerDto(ownerLastName, ownerFirstName, ownerMiddleName, mainPhone, optionalPhone,
            otherContacts, actualAddress, trustedMan, source, comment, rating);
    OwnerDto ownerDto = new OwnerDto(ownerId, ownerLastName, ownerFirstName, ownerMiddleName, mainPhone, optionalPhone,
            otherContacts, actualAddress, trustedMan, source, comment, rating, registrationDate, petsDto);

    OwnerShortDto ownerShortDto = new OwnerShortDto(ownerId, ownerLastName, ownerFirstName, ownerMiddleName, mainPhone,
            optionalPhone, registrationDate);

    @Captor
    private ArgumentCaptor<Owner> ownerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void addOwner() {
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
    void getSomeShortOwners() {
        Owner owner1 = new Owner(ownerId + 1, "1" + ownerLastName, "1" + ownerFirstName,
                "1" + ownerMiddleName, "1" + mainPhone, "1" + optionalPhone,
                "1" + otherContacts, "1" + actualAddress, "1" + trustedMan,
                "1" + source, "1" + comment, 1 + rating, registrationDate.plusHours(1), pets);
        Owner owner2 = new Owner(ownerId + 2, "2" + ownerLastName, "2" + ownerFirstName,
                "2" + ownerMiddleName, "2" + mainPhone, "2" + optionalPhone,
                "2" + otherContacts, "2" + actualAddress, "2" + trustedMan,
                "2" + source, "2" + comment, 2 + rating, registrationDate.plusHours(2), pets);
        Owner owner3 = new Owner(ownerId + 3, "3" + ownerLastName, "3" + ownerFirstName,
                "3" + ownerMiddleName, "3" + mainPhone, "3" + optionalPhone,
                "3" + otherContacts, "3" + actualAddress, "3" + trustedMan,
                "3" + source, "3" + comment, 3 + rating, registrationDate.plusHours(3), pets);

        Page<Owner> ownerList = new PageImpl<>(List.of(owner1, owner2, owner3));

        OwnerShortDto ownerShortDto1 = new OwnerShortDto(owner1.getId(), owner1.getLastName(), owner1.getFirstName(),
                owner1.getMiddleName(), owner1.getMainPhone(), owner1.getOptionalPhone(), owner1.getRegistrationDate());
        OwnerShortDto ownerShortDto2 = new OwnerShortDto(owner2.getId(), owner2.getLastName(), owner2.getFirstName(),
                owner2.getMiddleName(), owner2.getMainPhone(), owner2.getOptionalPhone(), owner2.getRegistrationDate());
        OwnerShortDto ownerShortDto3 = new OwnerShortDto(owner3.getId(), owner3.getLastName(), owner3.getFirstName(),
                owner3.getMiddleName(), owner3.getMainPhone(), owner3.getOptionalPhone(), owner3.getRegistrationDate());

        List<OwnerShortDto> ownerShortDtoList = List.of(ownerShortDto1, ownerShortDto2, ownerShortDto3);

        var num = 10;
        Pageable pageable = PageRequest.of(0, num, Sort.by("registrationDate").descending());

        when(ownerRepository.findAll(pageable)).thenReturn(ownerList);
        when(ownerMapper.shortMap(ownerList.toList())).thenReturn(ownerShortDtoList);

        List<OwnerShortDto> resultOwnersShortDto = ownerService.getSomeShortOwners(requesterId, num);

        assertAll(
                () -> assertEquals(ownerShortDtoList, resultOwnersShortDto),
                () -> verify(ownerRepository).findAll(pageable),
                () -> verify(ownerMapper).shortMap(ownerList.toList())
        );
    }

    @Test
    void getShortOwnerById() {
        when(entityService.getOwnerIfExists(ownerId)).thenReturn(owner);
        when(ownerMapper.toOwnerShortDto(owner)).thenReturn(ownerShortDto);

        OwnerShortDto resultOwnerShotDto = ownerService.getShortOwnerById(requesterId, ownerId);

        assertAll(
                () -> assertEquals(ownerShortDto, resultOwnerShotDto),
                () -> verify(entityService).getOwnerIfExists(ownerId),
                () -> verify(ownerMapper).toOwnerShortDto(owner)
        );
    }

    @Test
    void getOwnerById() {
        when(entityService.getOwnerIfExists(ownerId)).thenReturn(owner);
        when(ownerMapper.toOwnerDto(owner)).thenReturn(ownerDto);

        OwnerDto resultOwnerDto = ownerService.getOwnerById(requesterId, ownerId);

        assertAll(
                () -> assertEquals(ownerDto, resultOwnerDto),
                () -> verify(entityService).getOwnerIfExists(ownerId),
                () -> verify(ownerMapper).toOwnerDto(owner)
        );
    }

    @Test
    void updateUser_whenNewFieldsAllThenLastName_thenUpdateAllFieldsThanIdAndRegistrationDateAndLastName() {
        UpdateOwnerDto newOwnerDto = new UpdateOwnerDto(null, "upd " + ownerFirstName,
                "upd " + ownerMiddleName, "upd " + mainPhone, "upd " + optionalPhone,
                "upd " + otherContacts, "upd" + actualAddress, "upd " + trustedMan,
                "upd " + source, "upd " + comment, 1 + rating);

        Owner oldOwner = owner;

        Owner newOwner = new Owner(null, null, newOwnerDto.getFirstName(), newOwnerDto.getMiddleName(),
                newOwnerDto.getMainPhone(), newOwnerDto.getOptionalPhone(), newOwnerDto.getOtherContacts(),
                newOwnerDto.getActualAddress(), newOwnerDto.getTrustedMan(), newOwnerDto.getSource(),
                newOwnerDto.getComment(), newOwnerDto.getRating(), null, pets);

        Owner ownerAfter = new Owner(oldOwner.getId(), oldOwner.getFirstName(),
                newOwner.getLastName(), newOwner.getMiddleName(), newOwner.getMainPhone(), newOwner.getOptionalPhone(),
                newOwner.getOtherContacts(), newOwner.getActualAddress(), newOwner.getTrustedMan(),
                newOwner.getSource(), newOwner.getComment(), newOwner.getRating(),
                oldOwner.getRegistrationDate(), pets);

        OwnerDto ownerDtoAfter = new OwnerDto(ownerAfter.getId(), ownerAfter.getFirstName(), ownerAfter.getLastName(),
                ownerAfter.getMiddleName(), ownerAfter.getMainPhone(), ownerAfter.getOptionalPhone(),
                ownerAfter.getOtherContacts(), ownerAfter.getActualAddress(), ownerAfter.getTrustedMan(),
                ownerAfter.getSource(), ownerAfter.getComment(), ownerAfter.getRating(),
                ownerAfter.getRegistrationDate(), petsDto);

        when(entityService.getOwnerIfExists(ownerId)).thenReturn(oldOwner);
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(newOwner);
        when(ownerRepository.save(newOwner)).thenReturn(ownerAfter);
        when(ownerMapper.toOwnerDto(ownerAfter)).thenReturn(ownerDtoAfter);

        OwnerDto resultOwnerDto = ownerService.updateOwner(requesterId, ownerId, newOwnerDto);

        verify(ownerRepository).save(ownerArgumentCaptor.capture());
        Owner ownerForSave = ownerArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(ownerDtoAfter, resultOwnerDto, "entity test failed"),
                () -> assertEquals(oldOwner.getId(), ownerForSave.getId(),
                        "id field test failed"),
                () -> assertEquals(oldOwner.getLastName(), ownerForSave.getLastName(),
                        "LastName field test failed"),
                () -> assertEquals(newOwnerDto.getFirstName(), ownerForSave.getFirstName(),
                        "FirstName field test failed"),
                () -> assertEquals(newOwnerDto.getMiddleName(), ownerForSave.getMiddleName(),
                        "MiddleName field test failed"),
                () -> assertEquals(newOwnerDto.getMainPhone(), ownerForSave.getMainPhone(),
                        "MainPhone field test failed"),
                () -> assertEquals(newOwnerDto.getOptionalPhone(), ownerForSave.getOptionalPhone(),
                        "OptionalPhone field test failed"),
                () -> assertEquals(newOwnerDto.getOtherContacts(), ownerForSave.getOtherContacts(),
                        "OtherContacts field test failed"),
                () -> assertEquals(newOwnerDto.getActualAddress(), ownerForSave.getActualAddress(),
                        "ActualAddress field test failed"),
                () -> assertEquals(newOwnerDto.getTrustedMan(), ownerForSave.getTrustedMan(),
                        "TrustedMan field test failed"),
                () -> assertEquals(newOwnerDto.getSource(), ownerForSave.getSource(),
                        "Source field test failed"),
                () -> assertEquals(newOwnerDto.getComment(), ownerForSave.getComment(),
                        "Comment field test failed"),
                () -> assertEquals(newOwnerDto.getRating(), ownerForSave.getRating(),
                        "Rating field test failed"),
                () -> assertEquals(oldOwner.getRegistrationDate(), ownerForSave.getRegistrationDate(),
                        "RegistrationDate field test failed"),

                () -> verify(entityService).getOwnerIfExists(ownerId),
                () -> verify(ownerMapper).toOwner(newOwnerDto),
                () -> verify(ownerRepository).save(newOwner),
                () -> verify(ownerMapper).toOwnerDto(ownerAfter)
        );
    }

    @Test
    void updateUser_whenNewLastNameFieldsNotNull_thenUpdateNameFieldOnly() {
        UpdateOwnerDto newOwnerDto = UpdateOwnerDto.builder()
                .lastName("upd " + ownerLastName)
                .build();
        Owner oldOwner = owner;

        Owner newOwner = new Owner(null, newOwnerDto.getLastName(), newOwnerDto.getFirstName(),
                newOwnerDto.getMiddleName(), newOwnerDto.getMainPhone(), newOwnerDto.getOptionalPhone(),
                newOwnerDto.getOtherContacts(), newOwnerDto.getActualAddress(), newOwnerDto.getTrustedMan(),
                newOwnerDto.getSource(), newOwnerDto.getComment(), newOwnerDto.getRating(), null, pets);

        Owner ownerAfter = new Owner(oldOwner.getId(), oldOwner.getFirstName(),
                newOwner.getLastName(), newOwner.getMiddleName(), newOwner.getMainPhone(), newOwner.getOptionalPhone(),
                newOwner.getOtherContacts(), newOwner.getActualAddress(), newOwner.getTrustedMan(),
                newOwner.getSource(), newOwner.getComment(), newOwner.getRating(),
                oldOwner.getRegistrationDate(), pets);

        OwnerDto ownerDtoAfter = new OwnerDto(ownerAfter.getId(), ownerAfter.getFirstName(), ownerAfter.getLastName(),
                ownerAfter.getMiddleName(), ownerAfter.getMainPhone(), ownerAfter.getOptionalPhone(),
                ownerAfter.getOtherContacts(), ownerAfter.getActualAddress(), ownerAfter.getTrustedMan(),
                ownerAfter.getSource(), ownerAfter.getComment(), ownerAfter.getRating(),
                ownerAfter.getRegistrationDate(), petsDto);

        when(entityService.getOwnerIfExists(ownerId)).thenReturn(oldOwner);
        when(ownerMapper.toOwner(newOwnerDto)).thenReturn(newOwner);
        when(ownerRepository.save(newOwner)).thenReturn(ownerAfter);
        when(ownerMapper.toOwnerDto(ownerAfter)).thenReturn(ownerDtoAfter);

        OwnerDto resultOwnerDto = ownerService.updateOwner(requesterId, ownerId, newOwnerDto);

        verify(ownerRepository).save(ownerArgumentCaptor.capture());
        Owner ownerForSave = ownerArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(ownerDtoAfter, resultOwnerDto, "entity test failed"),
                () -> assertEquals(oldOwner.getId(), ownerForSave.getId(),
                        "id field test failed"),
                () -> assertEquals(newOwner.getLastName(), ownerForSave.getLastName(),
                        "LastName field test failed"),
                () -> assertEquals(oldOwner.getFirstName(), ownerForSave.getFirstName(),
                        "FirstName field test failed"),
                () -> assertEquals(oldOwner.getMiddleName(), ownerForSave.getMiddleName(),
                        "MiddleName field test failed"),
                () -> assertEquals(oldOwner.getMainPhone(), ownerForSave.getMainPhone(),
                        "MainPhone field test failed"),
                () -> assertEquals(oldOwner.getOptionalPhone(), ownerForSave.getOptionalPhone(),
                        "OptionalPhone field test failed"),
                () -> assertEquals(oldOwner.getOtherContacts(), ownerForSave.getOtherContacts(),
                        "OtherContacts field test failed"),
                () -> assertEquals(oldOwner.getActualAddress(), ownerForSave.getActualAddress(),
                        "ActualAddress field test failed"),
                () -> assertEquals(oldOwner.getTrustedMan(), ownerForSave.getTrustedMan(),
                        "TrustedMan field test failed"),
                () -> assertEquals(oldOwner.getSource(), ownerForSave.getSource(),
                        "Source field test failed"),
                () -> assertEquals(oldOwner.getComment(), ownerForSave.getComment(),
                        "Comment field test failed"),
                () -> assertEquals(oldOwner.getRating(), ownerForSave.getRating(),
                        "ёRating field test failed"),
                () -> assertEquals(oldOwner.getRegistrationDate(), ownerForSave.getRegistrationDate(),
                        "RegistrationDate field test failed"),

                () -> verify(entityService).getOwnerIfExists(ownerId),
                () -> verify(ownerMapper).toOwner(newOwnerDto),
                () -> verify(ownerRepository).save(newOwner),
                () -> verify(ownerMapper).toOwnerDto(ownerAfter)
        );
    }

    @Test
    void getAllOwners() {
        Owner owner1 = new Owner(ownerId + 1, "1" + ownerLastName, "1" + ownerFirstName,
                "1" + ownerMiddleName, "1" + mainPhone, "1" + optionalPhone,
                "1" + otherContacts, "1" + actualAddress, "1" + trustedMan,
                "1" + source, "1" + comment, 1 + rating, registrationDate.plusHours(1), pets);
        Owner owner2 = new Owner(ownerId + 2, "2" + ownerLastName, "2" + ownerFirstName,
                "2" + ownerMiddleName, "2" + mainPhone, "2" + optionalPhone,
                "2" + otherContacts, "2" + actualAddress, "2" + trustedMan,
                "2" + source, "2" + comment, 2 + rating, registrationDate.plusHours(2), pets);
        Owner owner3 = new Owner(ownerId + 3, "3" + ownerLastName, "3" + ownerFirstName,
                "3" + ownerMiddleName, "3" + mainPhone, "3" + optionalPhone,
                "3" + otherContacts, "3" + actualAddress, "3" + trustedMan,
                "3" + source, "3" + comment, 3 + rating, registrationDate.plusHours(3), pets);

        List<Owner> ownerList = List.of(owner1, owner2, owner3);

        OwnerDto ownerDto1 = new OwnerDto(owner1.getId(), owner1.getLastName(), owner1.getFirstName(),
                owner1.getMiddleName(), owner1.getMainPhone(), owner1.getOptionalPhone(), owner1.getOtherContacts(),
                owner1.getActualAddress(), owner1.getTrustedMan(), owner1.getSource(), owner1.getComment(),
                owner1.getRating(), owner1.getRegistrationDate(), petsDto);
        OwnerDto ownerDto2 = new OwnerDto(owner2.getId(), owner2.getLastName(), owner2.getFirstName(),
                owner2.getMiddleName(), owner2.getMainPhone(), owner2.getOptionalPhone(), owner2.getOtherContacts(),
                owner2.getActualAddress(), owner2.getTrustedMan(), owner2.getSource(), owner2.getComment(),
                owner2.getRating(), owner2.getRegistrationDate(), petsDto);
        OwnerDto ownerDto3 = new OwnerDto(owner3.getId(), owner3.getLastName(), owner3.getFirstName(),
                owner3.getMiddleName(), owner3.getMainPhone(), owner3.getOptionalPhone(), owner3.getOtherContacts(),
                owner3.getActualAddress(), owner3.getTrustedMan(), owner3.getSource(), owner3.getComment(),
                owner3.getRating(), owner3.getRegistrationDate(), petsDto);

        List<OwnerDto> ownerDtoList = List.of(ownerDto1, ownerDto2, ownerDto3);

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
    void deleteOwnerById() {
        when(ownerRepository.deleteOwnerById(ownerId)).thenReturn(1);

        ownerService.deleteOwnerById(requesterId, ownerId);

        verify(ownerRepository).deleteOwnerById(ownerId);
        verify(ownerRepository).deleteOwnerById(longArgumentCaptor.capture());
        Long idForDelete = longArgumentCaptor.getValue();
        assertEquals(ownerId, idForDelete);
    }

    @Test
    void checkOwnerPhoneNumber_whenOwnerAlreadyExist_thenReturnExistedOwner() {
        CheckOwnerDto checkOwnerDto = new CheckOwnerDto(mainPhone);
        when(ownerRepository.findByMainPhoneOrOptionalPhone(mainPhone, mainPhone)).thenReturn(Optional.of(owner));
        when(ownerMapper.toOwnerDto(owner)).thenReturn(ownerDto);

        OwnerDto resultOwnerDto = ownerService.checkOwnerPhoneNumber(requesterId, checkOwnerDto);

        assertAll(
                () -> verify(ownerRepository).findByMainPhoneOrOptionalPhone(mainPhone, mainPhone),
                () -> verify(ownerMapper).toOwnerDto(owner),
                () -> assertEquals(ownerDto, resultOwnerDto)
        );
    }

    @Test
    void checkOwnerPhoneNumber_whenOwnerNotyExist_thenReturnEmptyOwner() {
        CheckOwnerDto checkOwnerDto = new CheckOwnerDto(mainPhone);
        when(ownerRepository.findByMainPhoneOrOptionalPhone(mainPhone, mainPhone)).thenReturn(Optional.empty());
        when(ownerMapper.toOwnerDto(any(Owner.class))).thenReturn(new OwnerDto());

        OwnerDto resultOwnerDto = ownerService.checkOwnerPhoneNumber(requesterId, checkOwnerDto);

        assertAll(
                () -> assertEquals(new OwnerDto(), resultOwnerDto),
                () -> verify(ownerRepository).findByMainPhoneOrOptionalPhone(mainPhone, mainPhone),
                () -> verify(ownerMapper).toOwnerDto(any(Owner.class))
        );
    }

    @Test
    void searchOwner_whenOwnersFound_thenReturnFoundOwners() {
        SearchOwnerDto searchOwnerDto = new SearchOwnerDto("123");
        SearchDirection searchDirection = SearchDirection.PHONE;
        List<Owner> foundOwners = List.of(owner);
        when(ownerRepository.searchOwner(searchOwnerDto.getWanted(), searchDirection.getTitle())).thenReturn(foundOwners);
        when(ownerMapper.map(foundOwners)).thenReturn(List.of(ownerDto));

        Collection<OwnerDto> resultOwnersDto = ownerService.searchOwner(requesterId, searchOwnerDto, searchDirection);

        assertAll(
                () -> assertEquals(List.of(ownerDto), resultOwnersDto),
                () -> verify(ownerRepository).searchOwner(searchOwnerDto.getWanted(), searchDirection.getTitle()),
                () -> verify(ownerMapper).map(foundOwners)
        );
    }

    @Test
    void searchOwner_whenOwnersNotFound_thenReturnEmptyList() {
        SearchOwnerDto searchOwnerDto = new SearchOwnerDto("123");
        SearchDirection searchDirection = SearchDirection.PHONE;
        List<Owner> foundOwners = Collections.emptyList();
        when(ownerRepository.searchOwner(searchOwnerDto.getWanted(), searchDirection.getTitle())).thenReturn(foundOwners);
        when(ownerMapper.map(foundOwners)).thenReturn(Collections.emptyList());

        Collection<OwnerDto> resultOwnersDto = ownerService.searchOwner(requesterId, searchOwnerDto, searchDirection);

        assertAll(
                () -> assertEquals(Collections.emptyList(), resultOwnersDto),
                () -> verify(ownerRepository).searchOwner(searchOwnerDto.getWanted(), searchDirection.getTitle()),
                () -> verify(ownerMapper).map(foundOwners)
        );
    }
}
