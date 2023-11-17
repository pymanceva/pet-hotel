package ru.dogudacha.PetHotel.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.PetForAdminDto;
import ru.dogudacha.PetHotel.pet.model.Pet;

@Mapper(componentModel = "spring")
public interface PetMapper {
    @ToPet
    PetDto toPetDto(Pet pet);

    PetForAdminDto toPetForAdminDto(Pet pet);

    @Mapping(target = "id", ignore = true)
    Pet toPet(NewPetDto newPetDto);
}

