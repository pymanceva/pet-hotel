package ru.dogudacha.PetHotel.pet.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.dogudacha.PetHotel.exception.NotFoundException;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfPet;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
public class PetServiceIntegrationTest {
    private final EntityManager em;
    private final PetService service;

    final User requesterAdmin = User.builder()
            .email("admin@mail.ru")
            .name("admin")
            .role(Roles.ROLE_ADMIN)
            .build();

    final NewPetDto newPetDto = NewPetDto.builder()
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age("2 года")
            .build();

    final Pet pet = Pet.builder()
            .id(1L)
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age("2 года")
            .build();

    final UpdatePetDto updatePetDto = UpdatePetDto.builder()
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .age("12 лет")
            .build();

    @Test
    void createPet() {
        em.persist(requesterAdmin);
        PetDto actualPet = service.addPet(requesterAdmin.getId(), newPetDto);

        assertThat(actualPet.getId(), notNullValue());
        assertThat(actualPet.getBreed(), equalTo(newPetDto.getBreed()));
        assertThat(actualPet.getTypeOfPet(), equalTo(newPetDto.getTypeOfPet()));
        assertThat(actualPet.getSex(), equalTo(newPetDto.getSex()));
        assertThat(actualPet.getAge(), equalTo(newPetDto.getAge()));
    }

    @Test
    void updatePet() {
        em.persist(requesterAdmin);
        em.persist(pet);
        PetDto actualPet = service.updatePet(requesterAdmin.getId(), pet.getId(), updatePetDto);

        assertThat(actualPet.getId(), notNullValue());
        assertThat(actualPet.getBreed(), equalTo(updatePetDto.getBreed()));
        assertThat(actualPet.getTypeOfPet(), equalTo(updatePetDto.getTypeOfPet()));
        assertThat(actualPet.getSex(), equalTo(updatePetDto.getSex()));
        assertThat(actualPet.getAge(), equalTo(updatePetDto.getAge()));
    }


    @Test
    void getPetById() {
        em.persist(requesterAdmin);
        em.persist(pet);

        PetDto actualPet = service.getPetById(requesterAdmin.getId(), pet.getId());

        assertThat(actualPet.getId(), notNullValue());
        assertThat(actualPet.getBreed(), equalTo(pet.getBreed()));
        assertThat(actualPet.getTypeOfPet(), equalTo(pet.getTypeOfPet()));
        assertThat(actualPet.getSex(), equalTo(pet.getSex()));
        assertThat(actualPet.getAge(), equalTo(pet.getAge()));
    }


    @Test
    void deletePetById() {
        em.persist(requesterAdmin);
        em.persist(pet);

        service.deletePetById(requesterAdmin.getId(), pet.getId());

        String error = String.format("Pet with id = %d not found", pet.getId());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.getPetById(requesterAdmin.getId(), pet.getId())
        );

        assertEquals(error, exception.getMessage());
    }
}
