package ru.dogudacha.PetHotel.pet.service;

import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;

public interface PetService {
    PetDto addPet(Long requesterId, NewPetDto newPetDto);

    PetDto getPetById(Long requesterId, Long petId);

    PetDto updatePet(Long requesterId, Long petId, UpdatePetDto updatePetDto);

    void deletePetById(Long requesterId, Long petId);
}