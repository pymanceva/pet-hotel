package ru.modgy.pet.repository;

import ru.modgy.pet.dto.PetFilterParams;
import ru.modgy.pet.model.Pet;

import java.util.List;

public interface SearchPetRepository {
    List<Pet> findAllPetsByParams(PetFilterParams params);
}
