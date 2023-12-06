package ru.dogudacha.PetHotel.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dogudacha.PetHotel.pet.dto.NewPetDto;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.user.repository.UserRepository;

@Mapper(componentModel = "spring", uses = {UserRepository.class})
public interface PetMapper {

//    @Mapping(source = "pet.owner", target = "owner")
    PetDto toPetDto(Pet pet);

//    @Mapping(source = "newPetDto.ownerId", target = "owner", qualifiedBy = IdToUser.class)
    @Mapping(target = "id", ignore = true)
    Pet toPet(NewPetDto newPetDto);
}

