package ru.modgy.owner.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.modgy.owner.dto.NewOwnerDto;
import ru.modgy.owner.dto.OwnerDto;
import ru.modgy.owner.dto.OwnerShortDto;
import ru.modgy.owner.dto.UpdateOwnerDto;
import ru.modgy.owner.model.Owner;
import ru.modgy.pet.mapper.PetMapper;
import ru.modgy.utility.PhoneFormatMapper;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {PhoneFormatMapper.class, PetMapper.class})
public interface OwnerMapper {
    @Mapping(source = "mainPhone", target = "mainPhone", qualifiedByName = "formatPhoneNumber")
    @Mapping(source = "optionalPhone", target = "optionalPhone", qualifiedByName = "formatPhoneNumber")
    Owner toOwner(NewOwnerDto newOwnerDto);

    @Mapping(source = "mainPhone", target = "mainPhone", qualifiedByName = "formatPhoneNumber")
    @Mapping(source = "optionalPhone", target = "optionalPhone", qualifiedByName = "formatPhoneNumber")
    @Mapping(target = "id", ignore = true)
    Owner toOwner(UpdateOwnerDto updateOwnerDto);

    @Mapping(source = "pets", target = "petsDto")
    OwnerDto toOwnerDto(Owner owner);

    OwnerShortDto toOwnerShortDto(Owner owner);

    List<OwnerShortDto> shortMap(Collection<Owner> owners);

    List<OwnerDto> map(List<Owner> owners);
}
