package ru.modgy.pet.service;

import ru.modgy.pet.dto.NewPetDto;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.pet.dto.UpdatePetDto;

public interface PetService {
    PetDto addPet(Long requesterId, NewPetDto newPetDto);

    PetDto getPetById(Long requesterId, Long petId);

    PetDto updatePet(Long requesterId, Long petId, UpdatePetDto updatePetDto);

    void deletePetById(Long requesterId, Long petId);
}