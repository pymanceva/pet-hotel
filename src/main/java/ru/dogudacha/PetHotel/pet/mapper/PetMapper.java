package ru.dogudacha.PetHotel.pet.mapper;

import lombok.experimental.UtilityClass;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.dto.PetForAdminDto;
import ru.dogudacha.PetHotel.pet.model.Pet;

@UtilityClass
public class PetMapper {
    public Pet toPet(NewPetDto newPetDto) {
        return Pet.builder()
                .typeOfPet(newPetDto.getTypeOfPet())
                .breed(newPetDto.getBreed())
                .sex(newPetDto.getSex())
                .age(newPetDto.getAge())
                .weight(newPetDto.getWeight())
                .diet(newPetDto.getDiet())
                .isTakesMedications(newPetDto.getIsTakesMedications())
                .isContact(newPetDto.getIsContact())
                .isPhotographed(newPetDto.getIsPhotographed())
                .comments(newPetDto.getComments())
                .build();
    }

    public PetDto toPetDto(Pet pet) {
        return PetDto.builder()
                .id(pet.getId())
                .typeOfPet(pet.getTypeOfPet())
                .breed(pet.getBreed())
                .sex(pet.getSex())
                .age(pet.getAge())
                .weight(pet.getWeight())
                .diet(pet.getDiet())
                .isTakesMedications(pet.getIsTakesMedications())
                .isContact(pet.getIsContact())
                .isPhotographed(pet.getIsPhotographed())
                .comments(pet.getComments())
                .build();
    }

    public PetForAdminDto toPetForAdminDto(Pet pet) {
        return PetForAdminDto.builder()
                .id(pet.getId())
                .typeOfPet(pet.getTypeOfPet())
                .breed(pet.getBreed())
                .sex(pet.getSex())
                .age(pet.getAge())
                .weight(pet.getWeight())
                .diet(pet.getDiet())
                .isTakesMedications(pet.getIsTakesMedications())
                .isContact(pet.getIsContact())
                .isPhotographed(pet.getIsPhotographed())
                .comments(pet.getComments())
                .historyOfBookings(null)
                .additionalServices(null)
                .build();
    }
}

