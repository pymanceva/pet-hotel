package ru.dogudacha.PetHotel.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dogudacha.PetHotel.pet.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
//    Pet findByOwnerAndName(long ownerId, String namePet);

    Integer deletePetById(Long id);

}
