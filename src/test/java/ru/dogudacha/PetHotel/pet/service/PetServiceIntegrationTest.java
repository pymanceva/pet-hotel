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
import ru.dogudacha.PetHotel.pet.dto.PetForAdminDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfDiet;
import ru.dogudacha.PetHotel.user.model.Roles;
import ru.dogudacha.PetHotel.user.model.User;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
            .typeOfPet("Dog")
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age(2)
            .weight(7)
            .diet(TypeOfDiet.READY_INDUSTRIAL_FOOD)
            .isTakesMedications(false)
            .isContact(true)
            .isPhotographed(true)
            .build();

    final Pet pet = Pet.builder()
            .typeOfPet("Dog")
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age(2)
            .weight(7)
            .diet(TypeOfDiet.READY_INDUSTRIAL_FOOD)
            .isTakesMedications(false)
            .isContact(true)
            .isPhotographed(true)
            .build();

    final Pet pet2 = Pet.builder()
            .typeOfPet("Cat")
            .breed("No")
            .sex(Sex.MALE)
            .age(2)
            .weight(3)
            .diet(TypeOfDiet.READY_INDUSTRIAL_FOOD)
            .isTakesMedications(false)
            .isContact(true)
            .isPhotographed(true)
            .build();

    final UpdatePetDto updatePetDto = UpdatePetDto.builder()
            .typeOfPet("Dog small")
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .age(12)
            .weight(8)
            .diet(TypeOfDiet.NATURAL_RAW_FOOD)
            .isTakesMedications(true)
            .isContact(false)
            .isPhotographed(false)
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
        assertThat(actualPet.getWeight(), equalTo(newPetDto.getWeight()));
        assertThat(actualPet.getDiet(), equalTo(newPetDto.getDiet()));
        assertThat(actualPet.getIsTakesMedications(), equalTo(newPetDto.getIsTakesMedications()));
        assertThat(actualPet.getIsContact(), equalTo(newPetDto.getIsContact()));
        assertThat(actualPet.getIsPhotographed(), equalTo(newPetDto.getIsPhotographed()));
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
        assertThat(actualPet.getWeight(), equalTo(updatePetDto.getWeight()));
        assertThat(actualPet.getDiet(), equalTo(updatePetDto.getDiet()));
        assertThat(actualPet.getIsTakesMedications(), equalTo(updatePetDto.getIsTakesMedications()));
        assertThat(actualPet.getIsContact(), equalTo(updatePetDto.getIsContact()));
        assertThat(actualPet.getIsPhotographed(), equalTo(updatePetDto.getIsPhotographed()));
    }


    @Test
    void getPetById() {
        em.persist(requesterAdmin);
        em.persist(pet);

        PetForAdminDto actualPet = service.getPetByIdForAdmin(requesterAdmin.getId(), pet.getId());

        assertThat(actualPet.getId(), notNullValue());
        assertThat(actualPet.getBreed(), equalTo(pet.getBreed()));
        assertThat(actualPet.getTypeOfPet(), equalTo(pet.getTypeOfPet()));
        assertThat(actualPet.getSex(), equalTo(pet.getSex()));
        assertThat(actualPet.getAge(), equalTo(pet.getAge()));
        assertThat(actualPet.getWeight(), equalTo(pet.getWeight()));
        assertThat(actualPet.getDiet(), equalTo(pet.getDiet()));
        assertThat(actualPet.getIsTakesMedications(), equalTo(pet.getIsTakesMedications()));
        assertThat(actualPet.getIsContact(), equalTo(pet.getIsContact()));
        assertThat(actualPet.getIsPhotographed(), equalTo(pet.getIsPhotographed()));
    }

    @Test
    void getPets() {
        em.persist(requesterAdmin);
        em.persist(pet);
        em.persist(pet2);

        List<PetDto> actualPets = service.getAllPetsForAdmin(requesterAdmin.getId());

        assertThat(actualPets, hasSize(2));
        assertThat(actualPets.get(0).getId(), notNullValue());
        assertThat(actualPets.get(0).getBreed(), equalTo(pet.getBreed()));
        assertThat(actualPets.get(0).getTypeOfPet(), equalTo(pet.getTypeOfPet()));
        assertThat(actualPets.get(0).getSex(), equalTo(pet.getSex()));
        assertThat(actualPets.get(0).getAge(), equalTo(pet.getAge()));
        assertThat(actualPets.get(0).getWeight(), equalTo(pet.getWeight()));
        assertThat(actualPets.get(0).getDiet(), equalTo(pet.getDiet()));
        assertThat(actualPets.get(0).getIsTakesMedications(), equalTo(pet.getIsTakesMedications()));
        assertThat(actualPets.get(0).getIsContact(), equalTo(pet.getIsContact()));
        assertThat(actualPets.get(0).getIsPhotographed(), equalTo(pet.getIsPhotographed()));

        assertThat(actualPets.get(1).getId(), notNullValue());
        assertThat(actualPets.get(1).getBreed(), equalTo(pet2.getBreed()));
        assertThat(actualPets.get(1).getTypeOfPet(), equalTo(pet2.getTypeOfPet()));
        assertThat(actualPets.get(1).getSex(), equalTo(pet2.getSex()));
        assertThat(actualPets.get(1).getAge(), equalTo(pet2.getAge()));
        assertThat(actualPets.get(1).getWeight(), equalTo(pet2.getWeight()));
        assertThat(actualPets.get(1).getDiet(), equalTo(pet2.getDiet()));
        assertThat(actualPets.get(1).getIsTakesMedications(), equalTo(pet2.getIsTakesMedications()));
        assertThat(actualPets.get(1).getIsContact(), equalTo(pet2.getIsContact()));
        assertThat(actualPets.get(1).getIsPhotographed(), equalTo(pet2.getIsPhotographed()));
    }

    @Test
    void deletePetById() {
        em.persist(requesterAdmin);
        em.persist(pet);

        service.deletePetById(requesterAdmin.getId(), pet.getId());

        String error = String.format("Pet with id = %d not found", pet.getId());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.getPetByIdForAdmin(requesterAdmin.getId(), pet.getId())
        );

        assertEquals(error, exception.getMessage());
    }
}
