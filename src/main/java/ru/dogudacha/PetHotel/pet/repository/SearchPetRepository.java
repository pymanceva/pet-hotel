package ru.dogudacha.PetHotel.pet.repository;

import ru.dogudacha.PetHotel.pet.dto.PetFilterParams;
import ru.dogudacha.PetHotel.pet.model.Pet;

import java.util.List;

public interface SearchPetRepository {
    List<Pet> findAllPetsByParams(PetFilterParams params);
}
