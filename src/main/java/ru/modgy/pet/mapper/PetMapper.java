package ru.modgy.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.modgy.pet.dto.NewPetDto;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.pet.dto.PetDtoForOwner;
import ru.modgy.pet.dto.UpdatePetDto;
import ru.modgy.pet.model.Pet;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "ownerShortDto", ignore = true)
    @Mapping(source = "pet.birthDate", target = "age", qualifiedByName = "calculateAge")
    PetDto toPetDto(Pet pet);

    @Named("calculateAge")
    default String calculateAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        int ageYears = Period.between(birthDate, now).getYears();
        if (ageYears < 1) {
            int ageMonths = Period.between(birthDate, now).getMonths();
            if (ageMonths < 1) {
                int ageDays = Period.between(birthDate, now).getMonths();
                if (ageDays < 1) {
                    return String.format("дней : %d", ageDays);
                }
            } else {
                return String.format("месяцев : %d", ageMonths);
            }
        } else {
            return String.format("лет : %d", ageYears);
        }
        return null;
    }

    @Mapping(target = "id", ignore = true)
    Pet toPet(NewPetDto newPetDto);

    @Mapping(target = "id", ignore = true)
    Pet toPet(UpdatePetDto updatePetDto);

    Set<Pet> toPet(Collection<PetDto> pets);

    List<PetDto> toListPetDto(List<Pet> pets);

    List<PetDtoForOwner> toListPetDtoForOwner(List<Pet> pets);
}
