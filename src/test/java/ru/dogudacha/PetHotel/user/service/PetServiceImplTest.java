package ru.dogudacha.PetHotel.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.dogudacha.PetHotel.exception.AccessDeniedException;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.PetForAdminDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.mapper.PetMapper;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfDiet;
import ru.dogudacha.PetHotel.pet.repository.PetRepository;
import ru.dogudacha.PetHotel.pet.service.PetServiceImpl;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PetServiceImplTest {

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private PetServiceImpl petService;

    @Mock
    private PetRepository mockPetRepository;

    @Mock
    private PetMapper mockPetMapper;

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

    final NewPetDto newPetDto = NewPetDto.builder()
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

    final Pet pet = Pet.builder()
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

    final Pet pet2 = Pet.builder()
            .id(2L)
            .typeOfPet("Cat")
            .breed("No")
            .sex(Sex.MALE)
            .age(2)
            .weight(3)
            .diet(TypeOfDiet.READY_INDUSTRIAL_FOOD)
            .isTakesMedications(false)
            .isContact(true)
            .isPhotographed(true)
            .comments("Like play with mouse.")
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

    final Pet updatedPet = Pet.builder()
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

    final PetDto updatedPetDto = PetDto.builder()
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

    @Test
    void addPet_whenAddPetByAdmin_thenPetAdded() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetMapper.toPet(newPetDto)).thenReturn(pet);
        when(mockPetRepository.save(any())).thenReturn(pet);
        when(mockPetMapper.toPetDto(pet)).thenReturn(petDto);

        PetDto actualPetDto = petService.addPet(requesterAdmin.getId(), newPetDto);

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(petDto.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(petDto.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(petDto.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(petDto.getAge()));
        assertThat(actualPetDto.getWeight(), equalTo(petDto.getWeight()));
        assertThat(actualPetDto.getDiet(), equalTo(petDto.getDiet()));
        assertThat(actualPetDto.getIsTakesMedications(), equalTo(petDto.getIsTakesMedications()));
        assertThat(actualPetDto.getIsContact(), equalTo(petDto.getIsContact()));
        assertThat(actualPetDto.getIsPhotographed(), equalTo(petDto.getIsPhotographed()));
        assertThat(actualPetDto.getComments(), equalTo(petDto.getComments()));
        verify(mockPetRepository, times(1)).save(any());
    }

    @Test
    void addPet_whenAddPetByBoss_thenPetAdded() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetMapper.toPet(newPetDto)).thenReturn(pet);
        when(mockPetRepository.save(any())).thenReturn(pet);
        when(mockPetMapper.toPetDto(pet)).thenReturn(petDto);

        PetDto actualPetDto = petService.addPet(requesterBoss.getId(), newPetDto);

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(petDto.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(petDto.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(petDto.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(petDto.getAge()));
        assertThat(actualPetDto.getWeight(), equalTo(petDto.getWeight()));
        assertThat(actualPetDto.getDiet(), equalTo(petDto.getDiet()));
        assertThat(actualPetDto.getIsTakesMedications(), equalTo(petDto.getIsTakesMedications()));
        assertThat(actualPetDto.getIsContact(), equalTo(petDto.getIsContact()));
        assertThat(actualPetDto.getIsPhotographed(), equalTo(petDto.getIsPhotographed()));
        assertThat(actualPetDto.getComments(), equalTo(petDto.getComments()));
        verify(mockPetRepository, times(1)).save(any());
    }

    @Test
    void addPet_whenAddPetByUser_thenAccessDeniedExceptionThrown() {
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        when(mockPetMapper.toPet(newPetDto)).thenReturn(pet);
        when(mockPetMapper.toPetDto(pet)).thenReturn(petDto);
        String error = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> petService.addPet(requesterUser.getId(), newPetDto)
        );

        assertEquals(error, exception.getMessage());
        verify(mockPetRepository, times(0)).save(any());
    }

    @Test
    void addPet_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.addPet(userNotFoundId, newPetDto)
        );

        assertEquals(error, exception.getMessage());
        verify(mockPetRepository, times(0)).save(any());
    }

    @Test
    void getPetById_whenGetPetByAdmin_thenReturnPetForAdminDto() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockPetMapper.toPetForAdminDto(pet)).thenReturn(petForAdminDto);

        PetForAdminDto actualPetDto = petService.getPetByIdForAdmin(requesterAdmin.getId(), pet.getId());

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(petForAdminDto.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(petForAdminDto.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(petForAdminDto.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(petForAdminDto.getAge()));
        assertThat(actualPetDto.getWeight(), equalTo(petForAdminDto.getWeight()));
        assertThat(actualPetDto.getDiet(), equalTo(petForAdminDto.getDiet()));
        assertThat(actualPetDto.getIsTakesMedications(), equalTo(petForAdminDto.getIsTakesMedications()));
        assertThat(actualPetDto.getIsContact(), equalTo(petForAdminDto.getIsContact()));
        assertThat(actualPetDto.getIsPhotographed(), equalTo(petForAdminDto.getIsPhotographed()));
        assertThat(actualPetDto.getComments(), equalTo(petForAdminDto.getComments()));
        assertThat(actualPetDto.getHistoryOfBookings(), equalTo(petForAdminDto.getHistoryOfBookings()));
        assertThat(actualPetDto.getAdditionalServices(), equalTo(petForAdminDto.getAdditionalServices()));
    }

    @Test
    void getPetById_whenGetPetByBoss_thenReturnPetForAdminDto() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockPetMapper.toPetForAdminDto(pet)).thenReturn(petForAdminDto);

        PetForAdminDto actualPetDto = petService.getPetByIdForAdmin(requesterBoss.getId(), pet.getId());

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(petForAdminDto.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(petForAdminDto.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(petForAdminDto.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(petForAdminDto.getAge()));
        assertThat(actualPetDto.getWeight(), equalTo(petForAdminDto.getWeight()));
        assertThat(actualPetDto.getDiet(), equalTo(petForAdminDto.getDiet()));
        assertThat(actualPetDto.getIsTakesMedications(), equalTo(petForAdminDto.getIsTakesMedications()));
        assertThat(actualPetDto.getIsContact(), equalTo(petForAdminDto.getIsContact()));
        assertThat(actualPetDto.getIsPhotographed(), equalTo(petForAdminDto.getIsPhotographed()));
        assertThat(actualPetDto.getComments(), equalTo(petForAdminDto.getComments()));
        assertThat(actualPetDto.getHistoryOfBookings(), equalTo(petForAdminDto.getHistoryOfBookings()));
        assertThat(actualPetDto.getAdditionalServices(), equalTo(petForAdminDto.getAdditionalServices()));
    }

    @Test
    void getPetById_whenGetPetByUser_thenReturnPetDto() {
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockPetMapper.toPetDto(pet)).thenReturn(petDto);

        PetDto actualPetDto = petService.getPetByIdForUser(requesterUser.getId(), pet.getId());

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(petDto.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(petDto.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(petDto.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(petDto.getAge()));
        assertThat(actualPetDto.getWeight(), equalTo(petDto.getWeight()));
        assertThat(actualPetDto.getDiet(), equalTo(petDto.getDiet()));
        assertThat(actualPetDto.getIsTakesMedications(), equalTo(petDto.getIsTakesMedications()));
        assertThat(actualPetDto.getIsContact(), equalTo(petDto.getIsContact()));
        assertThat(actualPetDto.getIsPhotographed(), equalTo(petDto.getIsPhotographed()));
        assertThat(actualPetDto.getComments(), equalTo(petDto.getComments()));
    }

    @Test
    void getPetById_whenGetPetByBossAndPetNotFound_thenNotFoundExceptionThrown() {
        String error = String.format("Pet with id = %d not found", pet.getId());
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.getPetByIdForAdmin(requesterBoss.getId(), 0L)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void getPetById_whenGetPetByAdminAndPetNotFound_thenNotFoundExceptionThrown() {
        String error = String.format("Pet with id = %d not found", pet.getId());
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findById(any())).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.getPetByIdForAdmin(requesterAdmin.getId(), 0L)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void getPetById_whenGetPetByUserAndPetNotFound_thenNotFoundExceptionThrown() {
        String error = String.format("Pet with id = %d not found", pet.getId());
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        when(mockPetRepository.findById(any())).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.getPetByIdForUser(requesterUser.getId(), 0L)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void getPetById_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.getPetByIdForUser(userNotFoundId, pet.getId())
        );

        assertEquals(error, exception.getMessage());
        verify(mockPetRepository, times(0)).save(any());
    }

    @Test
    void updatePet_whenUpdatePetByBoss_thenReturnUpdatePetDto() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockPetRepository.save(any())).thenReturn(updatedPet);
        when(mockPetMapper.toPetDto(updatedPet)).thenReturn(updatedPetDto);


        PetDto actualPetDto = petService.updatePet(requesterBoss.getId(), pet.getId(), updatePet);

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(updatePet.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(updatePet.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(updatePet.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(updatePet.getAge()));
        assertThat(actualPetDto.getWeight(), equalTo(updatePet.getWeight()));
        assertThat(actualPetDto.getDiet(), equalTo(updatePet.getDiet()));
        assertThat(actualPetDto.getIsTakesMedications(), equalTo(updatePet.getIsTakesMedications()));
        assertThat(actualPetDto.getIsContact(), equalTo(updatePet.getIsContact()));
        assertThat(actualPetDto.getIsPhotographed(), equalTo(updatePet.getIsPhotographed()));
        assertThat(actualPetDto.getComments(), equalTo(updatePet.getComments()));
    }

    @Test
    void updatePet_whenUpdatePetByUser_thenAccessDeniedExceptionThrown() {
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));

        String error = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> petService.updatePet(requesterUser.getId(), pet.getId(), updatePet)
        );

        assertEquals(error, exception.getMessage());
        verify(mockPetRepository, times(0)).save(any());
    }

    @Test
    void updatePet_whenUpdatePetPetByAdmin_thenReturnUpdatePetDto() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockPetRepository.save(any())).thenReturn(updatedPet);
        when(mockPetMapper.toPetDto(updatedPet)).thenReturn(updatedPetDto);

        PetDto actualPetDto = petService.updatePet(requesterAdmin.getId(), pet.getId(), updatePet);

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(updatePet.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(updatePet.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(updatePet.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(updatePet.getAge()));
        assertThat(actualPetDto.getWeight(), equalTo(updatePet.getWeight()));
        assertThat(actualPetDto.getDiet(), equalTo(updatePet.getDiet()));
        assertThat(actualPetDto.getIsTakesMedications(), equalTo(updatePet.getIsTakesMedications()));
        assertThat(actualPetDto.getIsContact(), equalTo(updatePet.getIsContact()));
        assertThat(actualPetDto.getIsPhotographed(), equalTo(updatePet.getIsPhotographed()));
        assertThat(actualPetDto.getComments(), equalTo(updatePet.getComments()));
    }

    @Test
    void updatePet_whenUpdatePetPetByBossAndPetNotFound_thenNotFoundExceptionThrown() {
        String error = String.format("Pet with id = %d not found", pet.getId());
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.updatePet(requesterBoss.getId(), 0L, updatePet)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void updatePet_whenUpdatePetByAdminAndPetNotFound_thenNotFoundExceptionThrown() {
        String error = String.format("Pet with id = %d not found", pet.getId());
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findById(any())).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.updatePet(requesterAdmin.getId(), 0L, updatePet)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void updatePet_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.updatePet(userNotFoundId, pet.getId(), updatePet)
        );

        assertEquals(error, exception.getMessage());
        verify(mockPetRepository, times(0)).save(any());
    }

    @Test
    void getAllPets_whenGetPetsByAdmin_thenReturnListPets() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findAll()).thenReturn(List.of(pet, pet2));

        List<PetDto> actualPetDtos = petService.getAllPetsForAdmin(requesterAdmin.getId());

        assertNotNull(actualPetDtos);
        assertThat(actualPetDtos.size(), equalTo(2));
    }

    @Test
    void getAllPets_whenGetPetsByBoss_thenReturnListPets() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findAll()).thenReturn(List.of(pet, pet2));

        List<PetDto> actualPetDtos = petService.getAllPetsForAdmin(requesterBoss.getId());

        assertNotNull(actualPetDtos);
        assertThat(actualPetDtos.size(), equalTo(2));
    }

    @Test
    void getAllPets_whenGetPetsByAdmin_thenReturnEmptyListPets() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findAll()).thenReturn(Collections.emptyList());

        List<PetDto> actualPetDtos = petService.getAllPetsForAdmin(requesterAdmin.getId());

        assertNotNull(actualPetDtos);
        assertThat(actualPetDtos.size(), equalTo(0));
    }

    @Test
    void getAllPets_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.getAllPetsForAdmin(userNotFoundId)
        );

        assertEquals(error, exception.getMessage());
        verify(mockPetRepository, times(0)).save(any());
    }

    @Test
    void deletePetById_whenDeletePetByBoss_thenDeletedPet() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        doNothing().when(mockPetRepository).deleteById(any());

        petService.deletePetById(requesterBoss.getId(), pet.getId());

        verify(mockPetRepository, times(1)).deleteById(any());
    }

    @Test
    void deletePetById_whenDeletePetByAdmin_thenDeletedPet() {
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        doNothing().when(mockPetRepository).deleteById(any());

        petService.deletePetById(requesterAdmin.getId(), pet.getId());

        verify(mockPetRepository, times(1)).deleteById(any());
    }

    @Test
    void deletePetById_whenDeletePetByUser_thenAccessDeniedExceptionThrown() {
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        String error = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> petService.deletePetById(requesterUser.getId(), pet.getId()));

        assertEquals(error, exception.getMessage());
        verify(mockPetRepository, times(0)).deleteById(any());
    }

    @Test
    void deletePetById_whenDeletePetPetByBossAndPetNotFound_thenNotFoundExceptionThrown() {
        String error = String.format("Pet with id = %d not found", pet.getId());
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.deletePetById(requesterBoss.getId(), 0L)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void deletePetById_whenDeletePetByAdminAndPetNotFound_thenNotFoundExceptionThrown() {
        String error = String.format("Pet with id = %d not found", pet.getId());
        when(mockUserRepository.findById(requesterAdmin.getId())).thenReturn(Optional.of(requesterAdmin));
        when(mockPetRepository.findById(any())).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.deletePetById(requesterAdmin.getId(), 0L)
        );

        assertEquals(error, exception.getMessage());
    }

    @Test
    void deletePet_whenUserNotFound_thenNotFoundExceptionThrown() {
        long userNotFoundId = 0L;
        String error = String.format("User with id = %d not found", userNotFoundId);
        when(mockUserRepository.findById(userNotFoundId)).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.deletePetById(userNotFoundId, pet.getId())
        );

        assertEquals(error, exception.getMessage());
        verify(mockPetRepository, times(0)).save(any());
    }

}
