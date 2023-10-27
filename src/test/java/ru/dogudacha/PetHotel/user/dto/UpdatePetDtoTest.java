package ru.dogudacha.PetHotel.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.dogudacha.PetHotel.pet.dto.UpdatePetDto;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfDiet;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UpdatePetDtoTest {
    @Autowired
    private JacksonTester<UpdatePetDto> json;

    final UpdatePetDto updatePetDto = UpdatePetDto.builder()
            .typeOfPet("Dog small")
            .breed("Spaniel span")
            .sex(Sex.MALE)
            .age(12)
            .weight(8)
            .diet(TypeOfDiet.NATURAL_RAW_FOOD)
            .isTakesMedications(true)
            .isContact(false)
            .isPhotographed(false)
            .comments("Like play with small ball.")
            .build();

    @Test
    void testUpdatePetDto() throws Exception {
        JsonContent<UpdatePetDto> result = json.write(updatePetDto);

        assertThat(result).extractingJsonPathValue("$.typeOfPet").isNotNull();
        assertThat(result).extractingJsonPathValue("$.breed").isNotNull();
        assertThat(result).extractingJsonPathValue("$.sex").isNotNull();
        assertThat(result).extractingJsonPathValue("$.age").isNotNull();
        assertThat(result).extractingJsonPathValue("$.weight").isNotNull();
        assertThat(result).extractingJsonPathValue("$.diet").isNotNull();
        assertThat(result).extractingJsonPathValue("$.isTakesMedications").isNotNull();
        assertThat(result).extractingJsonPathValue("$.isContact").isNotNull();
        assertThat(result).extractingJsonPathValue("$.isPhotographed").isNotNull();
    }
}
