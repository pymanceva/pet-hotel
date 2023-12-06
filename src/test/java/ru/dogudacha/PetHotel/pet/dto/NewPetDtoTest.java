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
class NewPetDtoTest {
    @Autowired
    private JacksonTester<NewPetDto> json;

    final NewPetDto newPetDto = NewPetDto.builder()
            .typeOfPet(TypeOfPet.DOG)
            .breed("Spaniel")
            .sex(Sex.FEMALE)
            .age("2 года")
            .build();

    @Test
    void testNewPetDto() throws Exception {
        JsonContent<NewPetDto> result = json.write(newPetDto);

        assertThat(result).extractingJsonPathValue("$.typeOfPet").isNotNull();
        assertThat(result).extractingJsonPathValue("$.breed").isNotNull();
        assertThat(result).extractingJsonPathValue("$.sex").isNotNull();
        assertThat(result).extractingJsonPathValue("$.age").isNotNull();

    }
}
