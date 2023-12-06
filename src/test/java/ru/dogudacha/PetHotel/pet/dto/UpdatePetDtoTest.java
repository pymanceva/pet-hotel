package ru.dogudacha.PetHotel.pet.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfPet;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UpdatePetDtoTest {
    @Autowired
    private JacksonTester<UpdatePetDto> json;

    final UpdatePetDto updatePetDto = UpdatePetDto.builder()
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .age("12 лет")
            .build();

    @Test
    void testUpdatePetDto() throws Exception {
        JsonContent<UpdatePetDto> result = json.write(updatePetDto);

        assertThat(result).extractingJsonPathValue("$.typeOfPet").isNotNull();
        assertThat(result).extractingJsonPathValue("$.breed").isNotNull();
        assertThat(result).extractingJsonPathValue("$.sex").isNotNull();
        assertThat(result).extractingJsonPathValue("$.age").isNotNull();
    }
}
