package ru.dogudacha.PetHotel.pet.service;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;

public interface PetService {
    PetDto addPet(Long requesterId, NewPetDto newPetDto);

    PetDto getPetById(Long requesterId, Long petId);

    PetDto updatePet(Long requesterId, Long petId, UpdatePetDto updatePetDto);

    void deletePetById(Long requesterId, Long petId);

    Page<PetDto> getPetsBySearch(Long requesterId, String text, Integer page, Integer size);
}