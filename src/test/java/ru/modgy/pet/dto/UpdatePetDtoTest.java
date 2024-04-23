package ru.modgy.pet.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.modgy.pet.model.Sex;
import ru.modgy.pet.model.TypeOfPet;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UpdatePetDtoTest {
    @Autowired
    private JacksonTester<UpdatePetDto> json;

    private static final LocalDate BIRTH_DATE = LocalDate.now().minusYears(1);
    private static final LocalDate VET_VISIT_DATE = LocalDate.now().minusMonths(1);
    private static final LocalDate HEAT_DATE = LocalDate.now().plusMonths(1);

    final UpdatePetDto updatePetDto = UpdatePetDto.builder()
            .type(TypeOfPet.DOG)
            .name("Шарик")
            .breed("Spaniel")
            .birthDate(BIRTH_DATE)
            .sex(Sex.FEMALE)
            .color("black")
            .sign("Клеймо")
            .isExhibition(true)
            .vetVisitDate(VET_VISIT_DATE)
            .vetVisitReason("Ушиб")
            .vaccine("22.11.2023 антиклещ")
            .parasites("22.11.2023")
            .fleaMite("22.11.2023 антиклещ")
            .surgery("Кастрация")
            .pastDisease("пиелонефрит")
            .healthCharacteristic("без особенностей")
            .urineAnalysis("1.11.2023")
            .isAllergy(true)
            .allergyType("На молоко")
            .isChronicDisease(true)
            .chronicDiseaseType("Пиелонефрит")
            .heatDate(HEAT_DATE)
            .vetData("89000000000 Иван Иваныч Иванов")
            .stayWithoutMaster("Да, разлучается спокойно")
            .stayAlone("нет, воет, когда остаётся один")
            .specialCare("да, нужно расчёсывать шерсть два раза в день")
            .barkHowl("да")
            .furnitureDamage("нет")
            .foodFromTable("нет")
            .defecateAtHome("нет")
            .markAtHome("нет")
            .newPeople("дружелюбно")
            .isBitePeople(true)
            .reasonOfBite("Дразнил")
            .playWithDogs("да")
            .isDogTraining(true)
            .trainingName("Послушная собака")
            .like("Когда гладят по голове")
            .notLike("Когда трогают хвост")
            .toys("Мячик")
            .badHabit("Разбрасывает еду из миски")
            .walking("3 раза")
            .morningWalking("В 6 утра")
            .dayWalking("В 2 часа дня")
            .eveningWalking("В 8 часов вечера")
            .feedingQuantity(7)
            .feedType("сухой корм")
            .feedName("Довольный пёс")
            .feedComposition("говядина и овощи")
            .feedingRate("100 г")
            .feedingPractice("без особенностей")
            .treat("сахар")
            .isMedicine(true)
            .medicineRegimen("Здоровая собака")
            .additionalData("Любит, чтоб чесали животик")
            .build();

    @Test
    void testUpdatePetDto() throws Exception {
        JsonContent<UpdatePetDto> result = json.write(updatePetDto);

        assertThat(result).extractingJsonPathValue("$.type").isNotNull();
        assertThat(result).extractingJsonPathValue("$.name").isNotNull();
        assertThat(result).extractingJsonPathValue("$.breed").isNotNull();
        assertThat(result).extractingJsonPathValue("$.birthDate").isNotNull();
        assertThat(result).extractingJsonPathValue("$.sex").isNotNull();
        assertThat(result).extractingJsonPathValue("$.color").isNotNull();
        assertThat(result).extractingJsonPathValue("$.sign").isNotNull();
        assertThat(result).extractingJsonPathValue("$.isExhibition").isNotNull();
        assertThat(result).extractingJsonPathValue("$.vetVisitDate").isNotNull();
        assertThat(result).extractingJsonPathValue("$.vetVisitReason").isNotNull();
        assertThat(result).extractingJsonPathValue("$.vaccine").isNotNull();
        assertThat(result).extractingJsonPathValue("$.parasites").isNotNull();
        assertThat(result).extractingJsonPathValue("$.fleaMite").isNotNull();
        assertThat(result).extractingJsonPathValue("$.surgery").isNotNull();
        assertThat(result).extractingJsonPathValue("$.pastDisease").isNotNull();
        assertThat(result).extractingJsonPathValue("$.healthCharacteristic").isNotNull();
        assertThat(result).extractingJsonPathValue("$.urineAnalysis").isNotNull();
        assertThat(result).extractingJsonPathValue("$.isAllergy").isNotNull();
        assertThat(result).extractingJsonPathValue("$.allergyType").isNotNull();
        assertThat(result).extractingJsonPathValue("$.isChronicDisease").isNotNull();
        assertThat(result).extractingJsonPathValue("$.chronicDiseaseType").isNotNull();
        assertThat(result).extractingJsonPathValue("$.heatDate").isNotNull();
        assertThat(result).extractingJsonPathValue("$.vetData").isNotNull();
        assertThat(result).extractingJsonPathValue("$.stayWithoutMaster").isNotNull();
        assertThat(result).extractingJsonPathValue("$.stayAlone").isNotNull();
        assertThat(result).extractingJsonPathValue("$.specialCare").isNotNull();
        assertThat(result).extractingJsonPathValue("$.barkHowl").isNotNull();
        assertThat(result).extractingJsonPathValue("$.furnitureDamage").isNotNull();
        assertThat(result).extractingJsonPathValue("$.foodFromTable").isNotNull();
        assertThat(result).extractingJsonPathValue("$.defecateAtHome").isNotNull();
        assertThat(result).extractingJsonPathValue("$.markAtHome").isNotNull();
        assertThat(result).extractingJsonPathValue("$.newPeople").isNotNull();
        assertThat(result).extractingJsonPathValue("$.isBitePeople").isNotNull();
        assertThat(result).extractingJsonPathValue("$.reasonOfBite").isNotNull();
        assertThat(result).extractingJsonPathValue("$.playWithDogs").isNotNull();
        assertThat(result).extractingJsonPathValue("$.isDogTraining").isNotNull();
        assertThat(result).extractingJsonPathValue("$.trainingName").isNotNull();
        assertThat(result).extractingJsonPathValue("$.like").isNotNull();
        assertThat(result).extractingJsonPathValue("$.notLike").isNotNull();
        assertThat(result).extractingJsonPathValue("$.toys").isNotNull();
        assertThat(result).extractingJsonPathValue("$.badHabit").isNotNull();
        assertThat(result).extractingJsonPathValue("$.walking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.morningWalking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.dayWalking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.eveningWalking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.feedingQuantity").isNotNull();
        assertThat(result).extractingJsonPathValue("$.feedType").isNotNull();
        assertThat(result).extractingJsonPathValue("$.feedName").isNotNull();
        assertThat(result).extractingJsonPathValue("$.feedComposition").isNotNull();
        assertThat(result).extractingJsonPathValue("$.feedingRate").isNotNull();
        assertThat(result).extractingJsonPathValue("$.feedingPractice").isNotNull();
        assertThat(result).extractingJsonPathValue("$.treat").isNotNull();
        assertThat(result).extractingJsonPathValue("$.isMedicine").isNotNull();
        assertThat(result).extractingJsonPathValue("$.medicineRegimen").isNotNull();
        assertThat(result).extractingJsonPathValue("$.additionalData").isNotNull();
    }
}
