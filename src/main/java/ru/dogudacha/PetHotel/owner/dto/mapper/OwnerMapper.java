package ru.dogudacha.PetHotel.owner.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.dogudacha.PetHotel.owner.dto.NewOwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerDto;
import ru.dogudacha.PetHotel.owner.dto.OwnerShortDto;
import ru.dogudacha.PetHotel.owner.dto.UpdateOwnerDto;
import ru.dogudacha.PetHotel.owner.model.Owner;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OwnerMapper {
    OwnerDto toOwnerDto(Owner owner);

    OwnerShortDto toOwnerShortDto(Owner owner);

    @Mapping(target = "id", ignore = true)
    Owner toOwner(OwnerDto ownerDto);

    Owner toOwner(NewOwnerDto newOwnerDto);

    @Mapping(target = "id", ignore = true)
    Owner toOwner(UpdateOwnerDto updateOwnerDto);

    List<OwnerDto> map(List<Owner> owners);
}
