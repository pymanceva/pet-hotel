package ru.modgy.pet.service;

import ru.modgy.pet.dto.NewPetDto;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.pet.dto.UpdatePetDto;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;

public interface PetService {
    PetDto addPet(Long requesterId, NewPetDto newPetDto);

    PetDto getPetById(Long requesterId, Long petId);

    PetDto updatePet(Long requesterId, Long petId, UpdatePetDto updatePetDto);

    void deletePetById(Long requesterId, Long petId);

    Page<PetDto> getPetsBySearch(Long requesterId, String text, Integer page, Integer size);
}