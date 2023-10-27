package ru.dogudacha.PetHotel.pet.service;

import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.PetForAdminDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;

import java.util.List;

public interface PetService {
    PetDto addPet(Long requesterId, NewPetDto newPetDto);

    PetDto getPetByIdForUser(Long requesterId, Long petId);

    PetForAdminDto getPetByIdForAdmin(Long requesterId, Long petId);

    PetDto updatePet(Long requesterId, Long petId, UpdatePetDto updatePetDto);

    List<PetDto> getAllPetsForAdmin(Long requesterId);

    void deletePetById(Long requesterId, Long petId);
}
