package ru.modgy.pet.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.modgy.exception.NotFoundException;
import ru.modgy.pet.dto.NewPetDto;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.pet.dto.UpdatePetDto;
import ru.modgy.pet.model.Pet;
import ru.modgy.pet.model.Sex;
import ru.modgy.pet.model.TypeOfPet;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@ActiveProfiles("test")
public class PetServiceIntegrationTest {
    private final EntityManager em;
    private final PetService service;

    private static final LocalDate BIRTH_DATE = LocalDate.now().minusYears(1);
    private static final LocalDate VET_VISIT_DATE = LocalDate.now().minusMonths(1);
    private static final LocalDate HEAT_DATE = LocalDate.now().plusMonths(1);

    final User requesterAdmin = User.builder()
            .lastName("Кружкин")
            .firstName("admin")
            .middleName("Петрович")
            .email("admin@mail.ru")
            .password("admin_pwd")
            .role(Roles.ROLE_ADMIN)
            .isActive(true)
            .build();

    final NewPetDto newPetDto = NewPetDto.builder()
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

    final Pet pet = Pet.builder()
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


    final UpdatePetDto updatePetDto = UpdatePetDto.builder()
            .type(TypeOfPet.CAT)
            .name("Шар")
            .breed("Span")
            .birthDate(BIRTH_DATE.minusYears(1))
            .sex(Sex.MALE)
            .color("red")
            .sign("Чип")
            .isExhibition(false)
            .vetVisitDate(VET_VISIT_DATE.minusMonths(1))
            .vetVisitReason("Укус")
            .vaccine("22.09.2023 антиклещ")
            .parasites("22.09.2023")
            .fleaMite("22.09.2023 антиклещ")
            .surgery("Удаление бородавки")
            .pastDisease("отит")
            .healthCharacteristic("без особенностей")
            .urineAnalysis("1.09.2023")
            .isAllergy(true)
            .allergyType("На орехи")
            .isChronicDisease(true)
            .chronicDiseaseType("Гломерулонефрит")
            .heatDate(HEAT_DATE.plusDays(10))
            .vetData("89000000015 Иван Иваныч Иванов")
            .stayWithoutMaster("разлучается неспокойно")
            .stayAlone("да, ведёт себя спокойно, когда остаётся один")
            .specialCare("нет")
            .barkHowl("нет")
            .furnitureDamage("да")
            .foodFromTable("да")
            .defecateAtHome("да")
            .markAtHome("да")
            .newPeople("недружелюбно")
            .isBitePeople(true)
            .reasonOfBite("Дразнила")
            .playWithDogs("нет")
            .isDogTraining(true)
            .trainingName("Послушная кошка")
            .like("Когда гладят по спине")
            .notLike("Когда трогают уши")
            .toys("Мышка")
            .badHabit("Разбрасывает наполнитель из лотка")
            .walking("4 раз")
            .morningWalking("В 7 утра")
            .dayWalking("В 2 и 4 часа дня")
            .eveningWalking("В 9 часов вечера")
            .feedingQuantity(6)
            .feedType("консервы")
            .feedName("Довольный кот")
            .feedComposition("рыба")
            .feedingRate("10 г")
            .feedingPractice("нужно погреть корм")
            .treat("сметана")
            .isMedicine(true)
            .medicineRegimen("Здоровая кошка")
            .additionalData("Любит, чтоб чесали за ушком")
            .build();

    final Pet updatedPet = Pet.builder()
            .type(TypeOfPet.CAT)
            .name("Шар")
            .breed("Span")
            .birthDate(BIRTH_DATE.minusYears(1))
            .sex(Sex.MALE)
            .color("red")
            .sign("Чип")
            .isExhibition(false)
            .vetVisitDate(VET_VISIT_DATE.minusMonths(1))
            .vetVisitReason("Укус")
            .vaccine("22.09.2023 антиклещ")
            .parasites("22.09.2023")
            .fleaMite("22.09.2023 антиклещ")
            .surgery("Удаление бородавки")
            .pastDisease("отит")
            .healthCharacteristic("без особенностей")
            .urineAnalysis("1.09.2023")
            .isAllergy(true)
            .allergyType("На орехи")
            .isChronicDisease(true)
            .chronicDiseaseType("Гломерулонефрит")
            .heatDate(HEAT_DATE.plusDays(10))
            .vetData("89000000015 Иван Иваныч Иванов")
            .stayWithoutMaster("разлучается неспокойно")
            .stayAlone("да, ведёт себя спокойно, когда остаётся один")
            .specialCare("нет")
            .barkHowl("нет")
            .furnitureDamage("да")
            .foodFromTable("да")
            .defecateAtHome("да")
            .markAtHome("да")
            .newPeople("недружелюбно")
            .isBitePeople(true)
            .reasonOfBite("Дразнила")
            .playWithDogs("нет")
            .isDogTraining(true)
            .trainingName("Послушная кошка")
            .like("Когда гладят по спине")
            .notLike("Когда трогают уши")
            .toys("Мышка")
            .badHabit("Разбрасывает наполнитель из лотка")
            .walking("4 раз")
            .morningWalking("В 7 утра")
            .dayWalking("В 2 и 4 часа дня")
            .eveningWalking("В 9 часов вечера")
            .feedingQuantity(6)
            .feedType("консервы")
            .feedName("Довольный кот")
            .feedComposition("рыба")
            .feedingRate("10 г")
            .feedingPractice("нужно погреть корм")
            .treat("сметана")
            .isMedicine(true)
            .medicineRegimen("Здоровая кошка")
            .additionalData("Любит, чтоб чесали за ушком")
            .build();

    @Test
    void createPet() {
        System.out.println(requesterAdmin.toString());
        em.persist(requesterAdmin);
        PetDto actualPet = service.addPet(requesterAdmin.getId(), newPetDto);

        assertThat(actualPet.getId(), notNullValue());
        assertThat(actualPet.getType(), equalTo(pet.getType()));
        assertThat(actualPet.getName(), equalTo(pet.getName()));
        assertThat(actualPet.getBreed(), equalTo(pet.getBreed()));
        assertThat(actualPet.getBirthDate().isEqual(pet.getBirthDate()), is(true));
        assertThat(actualPet.getAge(), equalTo("лет : 1"));
        assertThat(actualPet.getSex(), equalTo(pet.getSex()));
        assertThat(actualPet.getColor(), equalTo(pet.getColor()));
        assertThat(actualPet.getSign(), equalTo(pet.getSign()));
        assertThat(actualPet.getIsExhibition(), equalTo(pet.getIsExhibition()));
        assertThat(actualPet.getVetVisitReason(), equalTo(pet.getVetVisitReason()));
        assertThat(actualPet.getVaccine(), equalTo(pet.getVaccine()));
        assertThat(actualPet.getParasites(), equalTo(pet.getParasites()));
        assertThat(actualPet.getFleaMite(), equalTo(pet.getFleaMite()));
        assertThat(actualPet.getSurgery(), equalTo(pet.getSurgery()));
        assertThat(actualPet.getPastDisease(), equalTo(pet.getPastDisease()));
        assertThat(actualPet.getHealthCharacteristic(), equalTo(pet.getHealthCharacteristic()));
        assertThat(actualPet.getUrineAnalysis(), equalTo(pet.getUrineAnalysis()));
        assertThat(actualPet.getIsAllergy(), equalTo(pet.getIsAllergy()));
        assertThat(actualPet.getAllergyType(), equalTo(pet.getAllergyType()));
        assertThat(actualPet.getIsChronicDisease(), equalTo(pet.getIsChronicDisease()));
        assertThat(actualPet.getChronicDiseaseType(), equalTo(pet.getChronicDiseaseType()));
        assertThat(actualPet.getHeatDate(), equalTo(pet.getHeatDate()));
        assertThat(actualPet.getVetData(), equalTo(pet.getVetData()));
        assertThat(actualPet.getStayWithoutMaster(), equalTo(pet.getStayWithoutMaster()));
        assertThat(actualPet.getStayAlone(), equalTo(pet.getStayAlone()));
        assertThat(actualPet.getSpecialCare(), equalTo(pet.getSpecialCare()));
        assertThat(actualPet.getBarkHowl(), equalTo(pet.getBarkHowl()));
        assertThat(actualPet.getFurnitureDamage(), equalTo(pet.getFurnitureDamage()));
        assertThat(actualPet.getFoodFromTable(), equalTo(pet.getFoodFromTable()));
        assertThat(actualPet.getDefecateAtHome(), equalTo(pet.getDefecateAtHome()));
        assertThat(actualPet.getMarkAtHome(), equalTo(pet.getMarkAtHome()));
        assertThat(actualPet.getNewPeople(), equalTo(pet.getNewPeople()));
        assertThat(actualPet.getIsBitePeople(), equalTo(pet.getIsBitePeople()));
        assertThat(actualPet.getReasonOfBite(), equalTo(pet.getReasonOfBite()));
        assertThat(actualPet.getPlayWithDogs(), equalTo(pet.getPlayWithDogs()));
        assertThat(actualPet.getIsDogTraining(), equalTo(pet.getIsDogTraining()));
        assertThat(actualPet.getTrainingName(), equalTo(pet.getTrainingName()));
        assertThat(actualPet.getLike(), equalTo(pet.getLike()));
        assertThat(actualPet.getNotLike(), equalTo(pet.getNotLike()));
        assertThat(actualPet.getToys(), equalTo(pet.getToys()));
        assertThat(actualPet.getBadHabit(), equalTo(pet.getBadHabit()));
        assertThat(actualPet.getWalking(), equalTo(pet.getWalking()));
        assertThat(actualPet.getMorningWalking(), equalTo(pet.getMorningWalking()));
        assertThat(actualPet.getDayWalking(), equalTo(pet.getDayWalking()));
        assertThat(actualPet.getEveningWalking(), equalTo(pet.getEveningWalking()));
        assertThat(actualPet.getFeedingQuantity(), equalTo(pet.getFeedingQuantity()));
        assertThat(actualPet.getFeedType(), equalTo(pet.getFeedType()));
        assertThat(actualPet.getFeedName(), equalTo(pet.getFeedName()));
        assertThat(actualPet.getFeedComposition(), equalTo(pet.getFeedComposition()));
        assertThat(actualPet.getFeedingRate(), equalTo(pet.getFeedingRate()));
        assertThat(actualPet.getFeedingPractice(), equalTo(pet.getFeedingPractice()));
        assertThat(actualPet.getTreat(), equalTo(pet.getTreat()));
        assertThat(actualPet.getIsMedicine(), equalTo(pet.getIsMedicine()));
        assertThat(actualPet.getMedicineRegimen(), equalTo(pet.getMedicineRegimen()));
        assertThat(actualPet.getAdditionalData(), equalTo(pet.getAdditionalData()));
    }

    @Test
    void updatePet() {
        em.persist(requesterAdmin);
        em.persist(pet);
        PetDto actualPet = service.updatePet(requesterAdmin.getId(), pet.getId(), updatePetDto);

        assertThat(actualPet.getId(), notNullValue());
        assertThat(actualPet.getType(), equalTo(updatedPet.getType()));
        assertThat(actualPet.getName(), equalTo(updatedPet.getName()));
        assertThat(actualPet.getBreed(), equalTo(updatedPet.getBreed()));
        assertThat(actualPet.getBirthDate().isEqual(updatedPet.getBirthDate()), is(true));
        assertThat(actualPet.getAge(), equalTo("лет : 2"));
        assertThat(actualPet.getSex(), equalTo(updatedPet.getSex()));
        assertThat(actualPet.getColor(), equalTo(updatedPet.getColor()));
        assertThat(actualPet.getSign(), equalTo(updatedPet.getSign()));
        assertThat(actualPet.getIsExhibition(), equalTo(updatedPet.getIsExhibition()));
        assertThat(actualPet.getVetVisitReason(), equalTo(updatedPet.getVetVisitReason()));
        assertThat(actualPet.getVaccine(), equalTo(updatedPet.getVaccine()));
        assertThat(actualPet.getParasites(), equalTo(updatedPet.getParasites()));
        assertThat(actualPet.getFleaMite(), equalTo(updatedPet.getFleaMite()));
        assertThat(actualPet.getSurgery(), equalTo(updatedPet.getSurgery()));
        assertThat(actualPet.getPastDisease(), equalTo(updatedPet.getPastDisease()));
        assertThat(actualPet.getHealthCharacteristic(), equalTo(updatedPet.getHealthCharacteristic()));
        assertThat(actualPet.getUrineAnalysis(), equalTo(updatedPet.getUrineAnalysis()));
        assertThat(actualPet.getIsAllergy(), equalTo(updatedPet.getIsAllergy()));
        assertThat(actualPet.getAllergyType(), equalTo(updatedPet.getAllergyType()));
        assertThat(actualPet.getIsChronicDisease(), equalTo(updatedPet.getIsChronicDisease()));
        assertThat(actualPet.getChronicDiseaseType(), equalTo(updatedPet.getChronicDiseaseType()));
        assertThat(actualPet.getHeatDate(), equalTo(updatedPet.getHeatDate()));
        assertThat(actualPet.getVetData(), equalTo(updatedPet.getVetData()));
        assertThat(actualPet.getStayWithoutMaster(), equalTo(updatedPet.getStayWithoutMaster()));
        assertThat(actualPet.getStayAlone(), equalTo(updatedPet.getStayAlone()));
        assertThat(actualPet.getSpecialCare(), equalTo(updatedPet.getSpecialCare()));
        assertThat(actualPet.getBarkHowl(), equalTo(updatedPet.getBarkHowl()));
        assertThat(actualPet.getFurnitureDamage(), equalTo(updatedPet.getFurnitureDamage()));
        assertThat(actualPet.getFoodFromTable(), equalTo(updatedPet.getFoodFromTable()));
        assertThat(actualPet.getDefecateAtHome(), equalTo(updatedPet.getDefecateAtHome()));
        assertThat(actualPet.getMarkAtHome(), equalTo(updatedPet.getMarkAtHome()));
        assertThat(actualPet.getNewPeople(), equalTo(updatedPet.getNewPeople()));
        assertThat(actualPet.getIsBitePeople(), equalTo(updatedPet.getIsBitePeople()));
        assertThat(actualPet.getReasonOfBite(), equalTo(updatedPet.getReasonOfBite()));
        assertThat(actualPet.getPlayWithDogs(), equalTo(updatedPet.getPlayWithDogs()));
        assertThat(actualPet.getIsDogTraining(), equalTo(updatedPet.getIsDogTraining()));
        assertThat(actualPet.getTrainingName(), equalTo(updatedPet.getTrainingName()));
        assertThat(actualPet.getLike(), equalTo(updatedPet.getLike()));
        assertThat(actualPet.getNotLike(), equalTo(updatedPet.getNotLike()));
        assertThat(actualPet.getToys(), equalTo(updatedPet.getToys()));
        assertThat(actualPet.getBadHabit(), equalTo(updatedPet.getBadHabit()));
        assertThat(actualPet.getWalking(), equalTo(updatedPet.getWalking()));
        assertThat(actualPet.getMorningWalking(), equalTo(updatedPet.getMorningWalking()));
        assertThat(actualPet.getDayWalking(), equalTo(updatedPet.getDayWalking()));
        assertThat(actualPet.getEveningWalking(), equalTo(updatedPet.getEveningWalking()));
        assertThat(actualPet.getFeedingQuantity(), equalTo(updatedPet.getFeedingQuantity()));
        assertThat(actualPet.getFeedType(), equalTo(updatedPet.getFeedType()));
        assertThat(actualPet.getFeedName(), equalTo(updatedPet.getFeedName()));
        assertThat(actualPet.getFeedComposition(), equalTo(updatedPet.getFeedComposition()));
        assertThat(actualPet.getFeedingRate(), equalTo(updatedPet.getFeedingRate()));
        assertThat(actualPet.getFeedingPractice(), equalTo(updatedPet.getFeedingPractice()));
        assertThat(actualPet.getTreat(), equalTo(updatedPet.getTreat()));
        assertThat(actualPet.getIsMedicine(), equalTo(updatedPet.getIsMedicine()));
        assertThat(actualPet.getMedicineRegimen(), equalTo(updatedPet.getMedicineRegimen()));
        assertThat(actualPet.getAdditionalData(), equalTo(updatedPet.getAdditionalData()));
    }

    @Test
    void getPetById() {
        em.persist(requesterAdmin);
        em.persist(pet);

        PetDto actualPet = service.getPetById(requesterAdmin.getId(), pet.getId());

        assertThat(actualPet.getId(), notNullValue());
        assertThat(actualPet.getType(), equalTo(pet.getType()));
        assertThat(actualPet.getName(), equalTo(pet.getName()));
        assertThat(actualPet.getBreed(), equalTo(pet.getBreed()));
        assertThat(actualPet.getBirthDate().isEqual(pet.getBirthDate()), is(true));
        assertThat(actualPet.getAge(), equalTo("лет : 1"));
        assertThat(actualPet.getSex(), equalTo(pet.getSex()));
        assertThat(actualPet.getColor(), equalTo(pet.getColor()));
        assertThat(actualPet.getSign(), equalTo(pet.getSign()));
        assertThat(actualPet.getIsExhibition(), equalTo(pet.getIsExhibition()));
        assertThat(actualPet.getVetVisitReason(), equalTo(pet.getVetVisitReason()));
        assertThat(actualPet.getVaccine(), equalTo(pet.getVaccine()));
        assertThat(actualPet.getParasites(), equalTo(pet.getParasites()));
        assertThat(actualPet.getFleaMite(), equalTo(pet.getFleaMite()));
        assertThat(actualPet.getSurgery(), equalTo(pet.getSurgery()));
        assertThat(actualPet.getPastDisease(), equalTo(pet.getPastDisease()));
        assertThat(actualPet.getHealthCharacteristic(), equalTo(pet.getHealthCharacteristic()));
        assertThat(actualPet.getUrineAnalysis(), equalTo(pet.getUrineAnalysis()));
        assertThat(actualPet.getIsAllergy(), equalTo(pet.getIsAllergy()));
        assertThat(actualPet.getAllergyType(), equalTo(pet.getAllergyType()));
        assertThat(actualPet.getIsChronicDisease(), equalTo(pet.getIsChronicDisease()));
        assertThat(actualPet.getChronicDiseaseType(), equalTo(pet.getChronicDiseaseType()));
        assertThat(actualPet.getHeatDate(), equalTo(pet.getHeatDate()));
        assertThat(actualPet.getVetData(), equalTo(pet.getVetData()));
        assertThat(actualPet.getStayWithoutMaster(), equalTo(pet.getStayWithoutMaster()));
        assertThat(actualPet.getStayAlone(), equalTo(pet.getStayAlone()));
        assertThat(actualPet.getSpecialCare(), equalTo(pet.getSpecialCare()));
        assertThat(actualPet.getBarkHowl(), equalTo(pet.getBarkHowl()));
        assertThat(actualPet.getFurnitureDamage(), equalTo(pet.getFurnitureDamage()));
        assertThat(actualPet.getFoodFromTable(), equalTo(pet.getFoodFromTable()));
        assertThat(actualPet.getDefecateAtHome(), equalTo(pet.getDefecateAtHome()));
        assertThat(actualPet.getMarkAtHome(), equalTo(pet.getMarkAtHome()));
        assertThat(actualPet.getNewPeople(), equalTo(pet.getNewPeople()));
        assertThat(actualPet.getIsBitePeople(), equalTo(pet.getIsBitePeople()));
        assertThat(actualPet.getReasonOfBite(), equalTo(pet.getReasonOfBite()));
        assertThat(actualPet.getPlayWithDogs(), equalTo(pet.getPlayWithDogs()));
        assertThat(actualPet.getIsDogTraining(), equalTo(pet.getIsDogTraining()));
        assertThat(actualPet.getTrainingName(), equalTo(pet.getTrainingName()));
        assertThat(actualPet.getLike(), equalTo(pet.getLike()));
        assertThat(actualPet.getNotLike(), equalTo(pet.getNotLike()));
        assertThat(actualPet.getToys(), equalTo(pet.getToys()));
        assertThat(actualPet.getBadHabit(), equalTo(pet.getBadHabit()));
        assertThat(actualPet.getWalking(), equalTo(pet.getWalking()));
        assertThat(actualPet.getMorningWalking(), equalTo(pet.getMorningWalking()));
        assertThat(actualPet.getDayWalking(), equalTo(pet.getDayWalking()));
        assertThat(actualPet.getEveningWalking(), equalTo(pet.getEveningWalking()));
        assertThat(actualPet.getFeedingQuantity(), equalTo(pet.getFeedingQuantity()));
        assertThat(actualPet.getFeedType(), equalTo(pet.getFeedType()));
        assertThat(actualPet.getFeedName(), equalTo(pet.getFeedName()));
        assertThat(actualPet.getFeedComposition(), equalTo(pet.getFeedComposition()));
        assertThat(actualPet.getFeedingRate(), equalTo(pet.getFeedingRate()));
        assertThat(actualPet.getFeedingPractice(), equalTo(pet.getFeedingPractice()));
        assertThat(actualPet.getTreat(), equalTo(pet.getTreat()));
        assertThat(actualPet.getIsMedicine(), equalTo(pet.getIsMedicine()));
        assertThat(actualPet.getMedicineRegimen(), equalTo(pet.getMedicineRegimen()));
        assertThat(actualPet.getAdditionalData(), equalTo(pet.getAdditionalData()));
    }

    @Test
    void deletePetById() {
        em.persist(requesterAdmin);
        em.persist(pet);

        service.deletePetById(requesterAdmin.getId(), pet.getId());

        String error = String.format("Pet with id = %d not found", pet.getId());
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.getPetById(requesterAdmin.getId(), pet.getId())
        );

        assertEquals(error, exception.getMessage());
    }
}
