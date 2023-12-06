package ru.dogudacha.PetHotel.pet.service;

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
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.mapper.PetMapper;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfPet;
import ru.dogudacha.PetHotel.pet.repository.PetRepository;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

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
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age("2 года")
            .build();

    final PetDto petDto = PetDto.builder()
            .id(1L)
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .build();


    final Pet pet = Pet.builder()
            .id(1L)
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age("2 года")
            .build();


    final UpdatePetDto updatePet = UpdatePetDto.builder()
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .age("12 лет")
            .build();

    final Pet updatedPet = Pet.builder()
            .id(1L)
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .age("12 лет")
            .build();

    final PetDto updatedPetDto = PetDto.builder()
            .id(1L)
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel span")
            .sex(Sex.MALE)
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
        when(mockPetMapper.toPetDto(pet)).thenReturn(petDto);

        PetDto actualPetDto = petService.getPetById(requesterAdmin.getId(), pet.getId());

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(petDto.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(petDto.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(petDto.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(petDto.getAge()));
    }

    @Test
    void getPetById_whenGetPetByBoss_thenReturnPetForAdminDto() {
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockPetMapper.toPetDto(pet)).thenReturn(petDto);

        PetDto actualPetDto = petService.getPetById(requesterBoss.getId(), pet.getId());

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(petDto.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(petDto.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(petDto.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(petDto.getAge()));
    }

    @Test
    void getPetById_whenGetPetByUser_thenReturnPetDto() {
        when(mockUserRepository.findById(requesterUser.getId())).thenReturn(Optional.of(requesterUser));
        when(mockPetRepository.findById(any())).thenReturn(Optional.of(pet));
        when(mockPetMapper.toPetDto(pet)).thenReturn(petDto);

        PetDto actualPetDto = petService.getPetById(requesterUser.getId(), pet.getId());

        assertNotNull(actualPetDto);
        assertThat(actualPetDto.getId(), equalTo(pet.getId()));
        assertThat(actualPetDto.getTypeOfPet(), equalTo(petDto.getTypeOfPet()));
        assertThat(actualPetDto.getBreed(), equalTo(petDto.getBreed()));
        assertThat(actualPetDto.getSex(), equalTo(petDto.getSex()));
        assertThat(actualPetDto.getAge(), equalTo(petDto.getAge()));
    }

    @Test
    void getPetById_whenGetPetByBossAndPetNotFound_thenNotFoundExceptionThrown() {
        String error = String.format("Pet with id = %d not found", pet.getId());
        when(mockUserRepository.findById(requesterBoss.getId())).thenReturn(Optional.of(requesterBoss));
        when(mockPetRepository.findById(any())).thenThrow(new NotFoundException(error));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> petService.getPetById(requesterBoss.getId(), 0L)
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
                () -> petService.getPetById(requesterAdmin.getId(), 0L)
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
                () -> petService.getPetById(requesterUser.getId(), 0L)
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
                () -> petService.getPetById(userNotFoundId, pet.getId())
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
