package ru.dogudacha.PetHotel.owner.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;
import ru.dogudacha.PetHotel.owner.model.Owner;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    OwnerDto toOwnerDto(Owner owner);

    @Mapping(target = "id", ignore = true)
    Owner toOwner(OwnerDto ownerDto);

    @Mapping(target = "id", ignore = true)
    Owner toOwner(UpdateOwnerDto updateOwnerDto);

    List<OwnerDto> map(List<Owner> owners);
}
