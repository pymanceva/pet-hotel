package ru.modgy.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.modgy.pet.model.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
//    Pet findByOwnerAndName(long ownerId, String namePet);

    Integer deletePetById(Long id);

    Optional<List<Pet>> findAllByIdIn(List<Long> petIds);

}
