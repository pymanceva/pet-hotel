package ru.modgy.pet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.modgy.exception.AccessDeniedException;
import ru.modgy.exception.NotFoundException;
import ru.modgy.pet.controller.PetController;
import ru.modgy.pet.dto.NewPetDto;
import ru.modgy.pet.dto.PetDto;
import ru.modgy.pet.dto.UpdatePetDto;
import ru.modgy.pet.model.Sex;
import ru.modgy.pet.model.TypeOfPet;
import ru.modgy.pet.service.PetService;
import ru.modgy.user.model.Roles;
import ru.modgy.user.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PetController.class)
public class PetControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    private final String requesterHeader = "X-PetHotel-User-Id";
    private static final LocalDate BIRTH_DATE = LocalDate.now().minusYears(1);
    private static final LocalDate VET_VISIT_DATE = LocalDate.now().minusMonths(1);
    private static final LocalDate HEAT_DATE = LocalDate.now().plusMonths(1);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    final User requesterBoss = User.builder()
            .email("boss@mail.ru")
            .firstName("boss")
            .id(1L)
            .role(Roles.ROLE_BOSS)
            .build();

    final User requesterAdmin = User.builder()
            .email("admin@mail.ru")
            .firstName("admin")
            .id(2L)
            .role(Roles.ROLE_ADMIN)
            .build();

    final User requesterUser = User.builder()
            .email("user@mail.ru")
            .firstName("user")
            .id(3L)
            .role(Roles.ROLE_USER)
            .build();

    final PetDto petDto = PetDto.builder()
            .id(1L)
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

    final UpdatePetDto updatePet = UpdatePetDto.builder()
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

    final PetDto updatedPet = PetDto.builder()
            .id(1L)
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
    @SneakyThrows
    void addPet() {
        when(petService.addPet(any(), any(NewPetDto.class))).thenReturn(petDto);

        mockMvc.perform(post("/pets")
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", is(petDto.getType().toString())))
                .andExpect(jsonPath("$.name", is(petDto.getName())))
                .andExpect(jsonPath("$.breed", is(petDto.getBreed())))
                .andExpect(jsonPath("$.birthDate", is(petDto.getBirthDate().format(formatter))))
                .andExpect(jsonPath("$.sex", is(petDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petDto.getAge())))
                .andExpect(jsonPath("$.color", is(petDto.getColor())))
                .andExpect(jsonPath("$.sign", is(petDto.getSign())))
                .andExpect(jsonPath("$.isExhibition", is(petDto.getIsExhibition())))
                .andExpect(jsonPath("$.vetVisitDate", is(petDto.getVetVisitDate().format(formatter))))
                .andExpect(jsonPath("$.vetVisitReason", is(petDto.getVetVisitReason())))
                .andExpect(jsonPath("$.vaccine", is(petDto.getVaccine())))
                .andExpect(jsonPath("$.parasites", is(petDto.getParasites())))
                .andExpect(jsonPath("$.fleaMite", is(petDto.getFleaMite())))
                .andExpect(jsonPath("$.surgery", is(petDto.getSurgery())))
                .andExpect(jsonPath("$.pastDisease", is(petDto.getPastDisease())))
                .andExpect(jsonPath("$.healthCharacteristic", is(petDto.getHealthCharacteristic())))
                .andExpect(jsonPath("$.urineAnalysis", is(petDto.getUrineAnalysis())))
                .andExpect(jsonPath("$.isAllergy", is(petDto.getIsAllergy())))
                .andExpect(jsonPath("$.allergyType", is(petDto.getAllergyType())))
                .andExpect(jsonPath("$.isChronicDisease", is(petDto.getIsChronicDisease())))
                .andExpect(jsonPath("$.chronicDiseaseType", is(petDto.getChronicDiseaseType())))
                .andExpect(jsonPath("$.heatDate", is(petDto.getHeatDate().format(formatter))))
                .andExpect(jsonPath("$.vetData", is(petDto.getVetData())))
                .andExpect(jsonPath("$.stayWithoutMaster", is(petDto.getStayWithoutMaster())))
                .andExpect(jsonPath("$.stayAlone", is(petDto.getStayAlone())))
                .andExpect(jsonPath("$.specialCare", is(petDto.getSpecialCare())))
                .andExpect(jsonPath("$.barkHowl", is(petDto.getBarkHowl())))
                .andExpect(jsonPath("$.furnitureDamage", is(petDto.getFurnitureDamage())))
                .andExpect(jsonPath("$.foodFromTable", is(petDto.getFoodFromTable())))
                .andExpect(jsonPath("$.defecateAtHome", is(petDto.getDefecateAtHome())))
                .andExpect(jsonPath("$.markAtHome", is(petDto.getMarkAtHome())))
                .andExpect(jsonPath("$.newPeople", is(petDto.getNewPeople())))
                .andExpect(jsonPath("$.isBitePeople", is(petDto.getIsBitePeople())))
                .andExpect(jsonPath("$.reasonOfBite", is(petDto.getReasonOfBite())))
                .andExpect(jsonPath("$.playWithDogs", is(petDto.getPlayWithDogs())))
                .andExpect(jsonPath("$.isDogTraining", is(petDto.getIsDogTraining())))
                .andExpect(jsonPath("$.trainingName", is(petDto.getTrainingName())))
                .andExpect(jsonPath("$.like", is(petDto.getLike())))
                .andExpect(jsonPath("$.notLike", is(petDto.getNotLike())))
                .andExpect(jsonPath("$.toys", is(petDto.getToys())))
                .andExpect(jsonPath("$.badHabit", is(petDto.getBadHabit())))
                .andExpect(jsonPath("$.walking", is(petDto.getWalking())))
                .andExpect(jsonPath("$.morningWalking", is(petDto.getMorningWalking())))
                .andExpect(jsonPath("$.dayWalking", is(petDto.getDayWalking())))
                .andExpect(jsonPath("$.eveningWalking", is(petDto.getEveningWalking())))
                .andExpect(jsonPath("$.feedingQuantity", is(petDto.getFeedingQuantity())))
                .andExpect(jsonPath("$.feedType", is(petDto.getFeedType())))
                .andExpect(jsonPath("$.feedName", is(petDto.getFeedName())))
                .andExpect(jsonPath("$.feedComposition", is(petDto.getFeedComposition())))
                .andExpect(jsonPath("$.feedingRate", is(petDto.getFeedingRate())))
                .andExpect(jsonPath("$.feedingPractice", is(petDto.getFeedingPractice())))
                .andExpect(jsonPath("$.treat", is(petDto.getTreat())))
                .andExpect(jsonPath("$.isMedicine", is(petDto.getIsMedicine())))
                .andExpect(jsonPath("$.medicineRegimen", is(petDto.getMedicineRegimen())))
                .andExpect(jsonPath("$.additionalData", is(petDto.getAdditionalData())));


        mockMvc.perform(post("/pets")
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", is(petDto.getType().toString())))
                .andExpect(jsonPath("$.name", is(petDto.getName())))
                .andExpect(jsonPath("$.breed", is(petDto.getBreed())))
                .andExpect(jsonPath("$.birthDate", is(petDto.getBirthDate().format(formatter))))
                .andExpect(jsonPath("$.sex", is(petDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petDto.getAge())))
                .andExpect(jsonPath("$.color", is(petDto.getColor())))
                .andExpect(jsonPath("$.sign", is(petDto.getSign())))
                .andExpect(jsonPath("$.isExhibition", is(petDto.getIsExhibition())))
                .andExpect(jsonPath("$.vetVisitDate", is(petDto.getVetVisitDate().format(formatter))))
                .andExpect(jsonPath("$.vetVisitReason", is(petDto.getVetVisitReason())))
                .andExpect(jsonPath("$.vaccine", is(petDto.getVaccine())))
                .andExpect(jsonPath("$.parasites", is(petDto.getParasites())))
                .andExpect(jsonPath("$.fleaMite", is(petDto.getFleaMite())))
                .andExpect(jsonPath("$.surgery", is(petDto.getSurgery())))
                .andExpect(jsonPath("$.pastDisease", is(petDto.getPastDisease())))
                .andExpect(jsonPath("$.healthCharacteristic", is(petDto.getHealthCharacteristic())))
                .andExpect(jsonPath("$.urineAnalysis", is(petDto.getUrineAnalysis())))
                .andExpect(jsonPath("$.isAllergy", is(petDto.getIsAllergy())))
                .andExpect(jsonPath("$.allergyType", is(petDto.getAllergyType())))
                .andExpect(jsonPath("$.isChronicDisease", is(petDto.getIsChronicDisease())))
                .andExpect(jsonPath("$.chronicDiseaseType", is(petDto.getChronicDiseaseType())))
                .andExpect(jsonPath("$.heatDate", is(petDto.getHeatDate().format(formatter))))
                .andExpect(jsonPath("$.vetData", is(petDto.getVetData())))
                .andExpect(jsonPath("$.stayWithoutMaster", is(petDto.getStayWithoutMaster())))
                .andExpect(jsonPath("$.stayAlone", is(petDto.getStayAlone())))
                .andExpect(jsonPath("$.specialCare", is(petDto.getSpecialCare())))
                .andExpect(jsonPath("$.barkHowl", is(petDto.getBarkHowl())))
                .andExpect(jsonPath("$.furnitureDamage", is(petDto.getFurnitureDamage())))
                .andExpect(jsonPath("$.foodFromTable", is(petDto.getFoodFromTable())))
                .andExpect(jsonPath("$.defecateAtHome", is(petDto.getDefecateAtHome())))
                .andExpect(jsonPath("$.markAtHome", is(petDto.getMarkAtHome())))
                .andExpect(jsonPath("$.newPeople", is(petDto.getNewPeople())))
                .andExpect(jsonPath("$.isBitePeople", is(petDto.getIsBitePeople())))
                .andExpect(jsonPath("$.reasonOfBite", is(petDto.getReasonOfBite())))
                .andExpect(jsonPath("$.playWithDogs", is(petDto.getPlayWithDogs())))
                .andExpect(jsonPath("$.isDogTraining", is(petDto.getIsDogTraining())))
                .andExpect(jsonPath("$.trainingName", is(petDto.getTrainingName())))
                .andExpect(jsonPath("$.like", is(petDto.getLike())))
                .andExpect(jsonPath("$.notLike", is(petDto.getNotLike())))
                .andExpect(jsonPath("$.toys", is(petDto.getToys())))
                .andExpect(jsonPath("$.badHabit", is(petDto.getBadHabit())))
                .andExpect(jsonPath("$.walking", is(petDto.getWalking())))
                .andExpect(jsonPath("$.morningWalking", is(petDto.getMorningWalking())))
                .andExpect(jsonPath("$.dayWalking", is(petDto.getDayWalking())))
                .andExpect(jsonPath("$.eveningWalking", is(petDto.getEveningWalking())))
                .andExpect(jsonPath("$.feedingQuantity", is(petDto.getFeedingQuantity())))
                .andExpect(jsonPath("$.feedType", is(petDto.getFeedType())))
                .andExpect(jsonPath("$.feedName", is(petDto.getFeedName())))
                .andExpect(jsonPath("$.feedComposition", is(petDto.getFeedComposition())))
                .andExpect(jsonPath("$.feedingRate", is(petDto.getFeedingRate())))
                .andExpect(jsonPath("$.feedingPractice", is(petDto.getFeedingPractice())))
                .andExpect(jsonPath("$.treat", is(petDto.getTreat())))
                .andExpect(jsonPath("$.isMedicine", is(petDto.getIsMedicine())))
                .andExpect(jsonPath("$.medicineRegimen", is(petDto.getMedicineRegimen())))
                .andExpect(jsonPath("$.additionalData", is(petDto.getAdditionalData())));


        String errorAccessDenied = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());
        when(petService.addPet(anyLong(), any())).thenThrow(new AccessDeniedException(errorAccessDenied));

        mockMvc.perform(post("/pets")
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andDo(print())
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/pets")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/pets")
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new PetDto())))
                .andExpect(status().isBadRequest());

        verify(petService, times(3)).addPet(anyLong(), any(NewPetDto.class));
    }

    @Test
    @SneakyThrows
    void getPetById() {
        when(petService.getPetById(anyLong(), anyLong())).thenReturn(petDto);

        mockMvc.perform(get("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", is(petDto.getType().toString())))
                .andExpect(jsonPath("$.name", is(petDto.getName())))
                .andExpect(jsonPath("$.breed", is(petDto.getBreed())))
                .andExpect(jsonPath("$.birthDate", is(petDto.getBirthDate().format(formatter))))
                .andExpect(jsonPath("$.sex", is(petDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petDto.getAge())))
                .andExpect(jsonPath("$.color", is(petDto.getColor())))
                .andExpect(jsonPath("$.sign", is(petDto.getSign())))
                .andExpect(jsonPath("$.isExhibition", is(petDto.getIsExhibition())))
                .andExpect(jsonPath("$.vetVisitDate", is(petDto.getVetVisitDate().format(formatter))))
                .andExpect(jsonPath("$.vetVisitReason", is(petDto.getVetVisitReason())))
                .andExpect(jsonPath("$.vaccine", is(petDto.getVaccine())))
                .andExpect(jsonPath("$.parasites", is(petDto.getParasites())))
                .andExpect(jsonPath("$.fleaMite", is(petDto.getFleaMite())))
                .andExpect(jsonPath("$.surgery", is(petDto.getSurgery())))
                .andExpect(jsonPath("$.pastDisease", is(petDto.getPastDisease())))
                .andExpect(jsonPath("$.healthCharacteristic", is(petDto.getHealthCharacteristic())))
                .andExpect(jsonPath("$.urineAnalysis", is(petDto.getUrineAnalysis())))
                .andExpect(jsonPath("$.isAllergy", is(petDto.getIsAllergy())))
                .andExpect(jsonPath("$.allergyType", is(petDto.getAllergyType())))
                .andExpect(jsonPath("$.isChronicDisease", is(petDto.getIsChronicDisease())))
                .andExpect(jsonPath("$.chronicDiseaseType", is(petDto.getChronicDiseaseType())))
                .andExpect(jsonPath("$.heatDate", is(petDto.getHeatDate().format(formatter))))
                .andExpect(jsonPath("$.vetData", is(petDto.getVetData())))
                .andExpect(jsonPath("$.stayWithoutMaster", is(petDto.getStayWithoutMaster())))
                .andExpect(jsonPath("$.stayAlone", is(petDto.getStayAlone())))
                .andExpect(jsonPath("$.specialCare", is(petDto.getSpecialCare())))
                .andExpect(jsonPath("$.barkHowl", is(petDto.getBarkHowl())))
                .andExpect(jsonPath("$.furnitureDamage", is(petDto.getFurnitureDamage())))
                .andExpect(jsonPath("$.foodFromTable", is(petDto.getFoodFromTable())))
                .andExpect(jsonPath("$.defecateAtHome", is(petDto.getDefecateAtHome())))
                .andExpect(jsonPath("$.markAtHome", is(petDto.getMarkAtHome())))
                .andExpect(jsonPath("$.newPeople", is(petDto.getNewPeople())))
                .andExpect(jsonPath("$.isBitePeople", is(petDto.getIsBitePeople())))
                .andExpect(jsonPath("$.reasonOfBite", is(petDto.getReasonOfBite())))
                .andExpect(jsonPath("$.playWithDogs", is(petDto.getPlayWithDogs())))
                .andExpect(jsonPath("$.isDogTraining", is(petDto.getIsDogTraining())))
                .andExpect(jsonPath("$.trainingName", is(petDto.getTrainingName())))
                .andExpect(jsonPath("$.like", is(petDto.getLike())))
                .andExpect(jsonPath("$.notLike", is(petDto.getNotLike())))
                .andExpect(jsonPath("$.toys", is(petDto.getToys())))
                .andExpect(jsonPath("$.badHabit", is(petDto.getBadHabit())))
                .andExpect(jsonPath("$.walking", is(petDto.getWalking())))
                .andExpect(jsonPath("$.morningWalking", is(petDto.getMorningWalking())))
                .andExpect(jsonPath("$.dayWalking", is(petDto.getDayWalking())))
                .andExpect(jsonPath("$.eveningWalking", is(petDto.getEveningWalking())))
                .andExpect(jsonPath("$.feedingQuantity", is(petDto.getFeedingQuantity())))
                .andExpect(jsonPath("$.feedType", is(petDto.getFeedType())))
                .andExpect(jsonPath("$.feedName", is(petDto.getFeedName())))
                .andExpect(jsonPath("$.feedComposition", is(petDto.getFeedComposition())))
                .andExpect(jsonPath("$.feedingRate", is(petDto.getFeedingRate())))
                .andExpect(jsonPath("$.feedingPractice", is(petDto.getFeedingPractice())))
                .andExpect(jsonPath("$.treat", is(petDto.getTreat())))
                .andExpect(jsonPath("$.isMedicine", is(petDto.getIsMedicine())))
                .andExpect(jsonPath("$.medicineRegimen", is(petDto.getMedicineRegimen())))
                .andExpect(jsonPath("$.additionalData", is(petDto.getAdditionalData())));

        mockMvc.perform(get("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", is(petDto.getType().toString())))
                .andExpect(jsonPath("$.name", is(petDto.getName())))
                .andExpect(jsonPath("$.breed", is(petDto.getBreed())))
                .andExpect(jsonPath("$.birthDate", is(petDto.getBirthDate().format(formatter))))
                .andExpect(jsonPath("$.sex", is(petDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petDto.getAge())))
                .andExpect(jsonPath("$.color", is(petDto.getColor())))
                .andExpect(jsonPath("$.sign", is(petDto.getSign())))
                .andExpect(jsonPath("$.isExhibition", is(petDto.getIsExhibition())))
                .andExpect(jsonPath("$.vetVisitDate", is(petDto.getVetVisitDate().format(formatter))))
                .andExpect(jsonPath("$.vetVisitReason", is(petDto.getVetVisitReason())))
                .andExpect(jsonPath("$.vaccine", is(petDto.getVaccine())))
                .andExpect(jsonPath("$.parasites", is(petDto.getParasites())))
                .andExpect(jsonPath("$.fleaMite", is(petDto.getFleaMite())))
                .andExpect(jsonPath("$.surgery", is(petDto.getSurgery())))
                .andExpect(jsonPath("$.pastDisease", is(petDto.getPastDisease())))
                .andExpect(jsonPath("$.healthCharacteristic", is(petDto.getHealthCharacteristic())))
                .andExpect(jsonPath("$.urineAnalysis", is(petDto.getUrineAnalysis())))
                .andExpect(jsonPath("$.isAllergy", is(petDto.getIsAllergy())))
                .andExpect(jsonPath("$.allergyType", is(petDto.getAllergyType())))
                .andExpect(jsonPath("$.isChronicDisease", is(petDto.getIsChronicDisease())))
                .andExpect(jsonPath("$.chronicDiseaseType", is(petDto.getChronicDiseaseType())))
                .andExpect(jsonPath("$.heatDate", is(petDto.getHeatDate().format(formatter))))
                .andExpect(jsonPath("$.vetData", is(petDto.getVetData())))
                .andExpect(jsonPath("$.stayWithoutMaster", is(petDto.getStayWithoutMaster())))
                .andExpect(jsonPath("$.stayAlone", is(petDto.getStayAlone())))
                .andExpect(jsonPath("$.specialCare", is(petDto.getSpecialCare())))
                .andExpect(jsonPath("$.barkHowl", is(petDto.getBarkHowl())))
                .andExpect(jsonPath("$.furnitureDamage", is(petDto.getFurnitureDamage())))
                .andExpect(jsonPath("$.foodFromTable", is(petDto.getFoodFromTable())))
                .andExpect(jsonPath("$.defecateAtHome", is(petDto.getDefecateAtHome())))
                .andExpect(jsonPath("$.markAtHome", is(petDto.getMarkAtHome())))
                .andExpect(jsonPath("$.newPeople", is(petDto.getNewPeople())))
                .andExpect(jsonPath("$.isBitePeople", is(petDto.getIsBitePeople())))
                .andExpect(jsonPath("$.reasonOfBite", is(petDto.getReasonOfBite())))
                .andExpect(jsonPath("$.playWithDogs", is(petDto.getPlayWithDogs())))
                .andExpect(jsonPath("$.isDogTraining", is(petDto.getIsDogTraining())))
                .andExpect(jsonPath("$.trainingName", is(petDto.getTrainingName())))
                .andExpect(jsonPath("$.like", is(petDto.getLike())))
                .andExpect(jsonPath("$.notLike", is(petDto.getNotLike())))
                .andExpect(jsonPath("$.toys", is(petDto.getToys())))
                .andExpect(jsonPath("$.badHabit", is(petDto.getBadHabit())))
                .andExpect(jsonPath("$.walking", is(petDto.getWalking())))
                .andExpect(jsonPath("$.morningWalking", is(petDto.getMorningWalking())))
                .andExpect(jsonPath("$.dayWalking", is(petDto.getDayWalking())))
                .andExpect(jsonPath("$.eveningWalking", is(petDto.getEveningWalking())))
                .andExpect(jsonPath("$.feedingQuantity", is(petDto.getFeedingQuantity())))
                .andExpect(jsonPath("$.feedType", is(petDto.getFeedType())))
                .andExpect(jsonPath("$.feedName", is(petDto.getFeedName())))
                .andExpect(jsonPath("$.feedComposition", is(petDto.getFeedComposition())))
                .andExpect(jsonPath("$.feedingRate", is(petDto.getFeedingRate())))
                .andExpect(jsonPath("$.feedingPractice", is(petDto.getFeedingPractice())))
                .andExpect(jsonPath("$.treat", is(petDto.getTreat())))
                .andExpect(jsonPath("$.isMedicine", is(petDto.getIsMedicine())))
                .andExpect(jsonPath("$.medicineRegimen", is(petDto.getMedicineRegimen())))
                .andExpect(jsonPath("$.additionalData", is(petDto.getAdditionalData())));

        when(petService.getPetById(anyLong(), anyLong())).thenReturn(petDto);

        mockMvc.perform(get("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", is(petDto.getType().toString())))
                .andExpect(jsonPath("$.name", is(petDto.getName())))
                .andExpect(jsonPath("$.breed", is(petDto.getBreed())))
                .andExpect(jsonPath("$.birthDate", is(petDto.getBirthDate().format(formatter))))
                .andExpect(jsonPath("$.sex", is(petDto.getSex().toString())))
                .andExpect(jsonPath("$.age", is(petDto.getAge())))
                .andExpect(jsonPath("$.color", is(petDto.getColor())))
                .andExpect(jsonPath("$.sign", is(petDto.getSign())))
                .andExpect(jsonPath("$.isExhibition", is(petDto.getIsExhibition())))
                .andExpect(jsonPath("$.vetVisitDate", is(petDto.getVetVisitDate().format(formatter))))
                .andExpect(jsonPath("$.vetVisitReason", is(petDto.getVetVisitReason())))
                .andExpect(jsonPath("$.vaccine", is(petDto.getVaccine())))
                .andExpect(jsonPath("$.parasites", is(petDto.getParasites())))
                .andExpect(jsonPath("$.fleaMite", is(petDto.getFleaMite())))
                .andExpect(jsonPath("$.surgery", is(petDto.getSurgery())))
                .andExpect(jsonPath("$.pastDisease", is(petDto.getPastDisease())))
                .andExpect(jsonPath("$.healthCharacteristic", is(petDto.getHealthCharacteristic())))
                .andExpect(jsonPath("$.urineAnalysis", is(petDto.getUrineAnalysis())))
                .andExpect(jsonPath("$.isAllergy", is(petDto.getIsAllergy())))
                .andExpect(jsonPath("$.allergyType", is(petDto.getAllergyType())))
                .andExpect(jsonPath("$.isChronicDisease", is(petDto.getIsChronicDisease())))
                .andExpect(jsonPath("$.chronicDiseaseType", is(petDto.getChronicDiseaseType())))
                .andExpect(jsonPath("$.heatDate", is(petDto.getHeatDate().format(formatter))))
                .andExpect(jsonPath("$.vetData", is(petDto.getVetData())))
                .andExpect(jsonPath("$.stayWithoutMaster", is(petDto.getStayWithoutMaster())))
                .andExpect(jsonPath("$.stayAlone", is(petDto.getStayAlone())))
                .andExpect(jsonPath("$.specialCare", is(petDto.getSpecialCare())))
                .andExpect(jsonPath("$.barkHowl", is(petDto.getBarkHowl())))
                .andExpect(jsonPath("$.furnitureDamage", is(petDto.getFurnitureDamage())))
                .andExpect(jsonPath("$.foodFromTable", is(petDto.getFoodFromTable())))
                .andExpect(jsonPath("$.defecateAtHome", is(petDto.getDefecateAtHome())))
                .andExpect(jsonPath("$.markAtHome", is(petDto.getMarkAtHome())))
                .andExpect(jsonPath("$.newPeople", is(petDto.getNewPeople())))
                .andExpect(jsonPath("$.isBitePeople", is(petDto.getIsBitePeople())))
                .andExpect(jsonPath("$.reasonOfBite", is(petDto.getReasonOfBite())))
                .andExpect(jsonPath("$.playWithDogs", is(petDto.getPlayWithDogs())))
                .andExpect(jsonPath("$.isDogTraining", is(petDto.getIsDogTraining())))
                .andExpect(jsonPath("$.trainingName", is(petDto.getTrainingName())))
                .andExpect(jsonPath("$.like", is(petDto.getLike())))
                .andExpect(jsonPath("$.notLike", is(petDto.getNotLike())))
                .andExpect(jsonPath("$.toys", is(petDto.getToys())))
                .andExpect(jsonPath("$.badHabit", is(petDto.getBadHabit())))
                .andExpect(jsonPath("$.walking", is(petDto.getWalking())))
                .andExpect(jsonPath("$.morningWalking", is(petDto.getMorningWalking())))
                .andExpect(jsonPath("$.dayWalking", is(petDto.getDayWalking())))
                .andExpect(jsonPath("$.eveningWalking", is(petDto.getEveningWalking())))
                .andExpect(jsonPath("$.feedingQuantity", is(petDto.getFeedingQuantity())))
                .andExpect(jsonPath("$.feedType", is(petDto.getFeedType())))
                .andExpect(jsonPath("$.feedName", is(petDto.getFeedName())))
                .andExpect(jsonPath("$.feedComposition", is(petDto.getFeedComposition())))
                .andExpect(jsonPath("$.feedingRate", is(petDto.getFeedingRate())))
                .andExpect(jsonPath("$.feedingPractice", is(petDto.getFeedingPractice())))
                .andExpect(jsonPath("$.treat", is(petDto.getTreat())))
                .andExpect(jsonPath("$.isMedicine", is(petDto.getIsMedicine())))
                .andExpect(jsonPath("$.medicineRegimen", is(petDto.getMedicineRegimen())))
                .andExpect(jsonPath("$.additionalData", is(petDto.getAdditionalData())));

        mockMvc.perform(get("/pets/{id}", petDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        String errorNotFoundPet = String.format("Pet with id = %d not found", petDto.getId());
        when(petService.getPetById(anyLong(), eq(0L))).thenThrow(new NotFoundException(errorNotFoundPet));

        mockMvc.perform(get("/pets/{id}/admin", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(petService, times(3)).getPetById(anyLong(), anyLong());
    }


    @Test
    @SneakyThrows
    void updatePet() {
        when(petService.updatePet(anyLong(), anyLong(), any(UpdatePetDto.class))).thenReturn(updatedPet);

        mockMvc.perform(patch("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", is(updatePet.getType().toString())))
                .andExpect(jsonPath("$.name", is(updatePet.getName())))
                .andExpect(jsonPath("$.breed", is(updatePet.getBreed())))
                .andExpect(jsonPath("$.birthDate", is(updatePet.getBirthDate().format(formatter))))
                .andExpect(jsonPath("$.sex", is(updatePet.getSex().toString())))
                .andExpect(jsonPath("$.age", is(updatedPet.getAge())))
                .andExpect(jsonPath("$.color", is(updatePet.getColor())))
                .andExpect(jsonPath("$.sign", is(updatePet.getSign())))
                .andExpect(jsonPath("$.isExhibition", is(updatePet.getIsExhibition())))
                .andExpect(jsonPath("$.vetVisitDate", is(updatePet.getVetVisitDate().format(formatter))))
                .andExpect(jsonPath("$.vetVisitReason", is(updatePet.getVetVisitReason())))
                .andExpect(jsonPath("$.vaccine", is(updatePet.getVaccine())))
                .andExpect(jsonPath("$.parasites", is(updatePet.getParasites())))
                .andExpect(jsonPath("$.fleaMite", is(updatePet.getFleaMite())))
                .andExpect(jsonPath("$.surgery", is(updatePet.getSurgery())))
                .andExpect(jsonPath("$.pastDisease", is(updatePet.getPastDisease())))
                .andExpect(jsonPath("$.healthCharacteristic", is(updatePet.getHealthCharacteristic())))
                .andExpect(jsonPath("$.urineAnalysis", is(updatePet.getUrineAnalysis())))
                .andExpect(jsonPath("$.isAllergy", is(updatePet.getIsAllergy())))
                .andExpect(jsonPath("$.allergyType", is(updatePet.getAllergyType())))
                .andExpect(jsonPath("$.isChronicDisease", is(updatePet.getIsChronicDisease())))
                .andExpect(jsonPath("$.chronicDiseaseType", is(updatePet.getChronicDiseaseType())))
                .andExpect(jsonPath("$.heatDate", is(updatePet.getHeatDate().format(formatter))))
                .andExpect(jsonPath("$.vetData", is(updatePet.getVetData())))
                .andExpect(jsonPath("$.stayWithoutMaster", is(updatePet.getStayWithoutMaster())))
                .andExpect(jsonPath("$.stayAlone", is(updatePet.getStayAlone())))
                .andExpect(jsonPath("$.specialCare", is(updatePet.getSpecialCare())))
                .andExpect(jsonPath("$.barkHowl", is(updatePet.getBarkHowl())))
                .andExpect(jsonPath("$.furnitureDamage", is(updatePet.getFurnitureDamage())))
                .andExpect(jsonPath("$.foodFromTable", is(updatePet.getFoodFromTable())))
                .andExpect(jsonPath("$.defecateAtHome", is(updatePet.getDefecateAtHome())))
                .andExpect(jsonPath("$.markAtHome", is(updatePet.getMarkAtHome())))
                .andExpect(jsonPath("$.newPeople", is(updatePet.getNewPeople())))
                .andExpect(jsonPath("$.isBitePeople", is(updatePet.getIsBitePeople())))
                .andExpect(jsonPath("$.reasonOfBite", is(updatePet.getReasonOfBite())))
                .andExpect(jsonPath("$.playWithDogs", is(updatePet.getPlayWithDogs())))
                .andExpect(jsonPath("$.isDogTraining", is(updatePet.getIsDogTraining())))
                .andExpect(jsonPath("$.trainingName", is(updatePet.getTrainingName())))
                .andExpect(jsonPath("$.like", is(updatePet.getLike())))
                .andExpect(jsonPath("$.notLike", is(updatePet.getNotLike())))
                .andExpect(jsonPath("$.toys", is(updatePet.getToys())))
                .andExpect(jsonPath("$.badHabit", is(updatePet.getBadHabit())))
                .andExpect(jsonPath("$.walking", is(updatePet.getWalking())))
                .andExpect(jsonPath("$.morningWalking", is(updatePet.getMorningWalking())))
                .andExpect(jsonPath("$.dayWalking", is(updatePet.getDayWalking())))
                .andExpect(jsonPath("$.eveningWalking", is(updatePet.getEveningWalking())))
                .andExpect(jsonPath("$.feedingQuantity", is(updatePet.getFeedingQuantity())))
                .andExpect(jsonPath("$.feedType", is(updatePet.getFeedType())))
                .andExpect(jsonPath("$.feedName", is(updatePet.getFeedName())))
                .andExpect(jsonPath("$.feedComposition", is(updatePet.getFeedComposition())))
                .andExpect(jsonPath("$.feedingRate", is(updatePet.getFeedingRate())))
                .andExpect(jsonPath("$.feedingPractice", is(updatePet.getFeedingPractice())))
                .andExpect(jsonPath("$.treat", is(updatePet.getTreat())))
                .andExpect(jsonPath("$.isMedicine", is(updatePet.getIsMedicine())))
                .andExpect(jsonPath("$.medicineRegimen", is(updatePet.getMedicineRegimen())))
                .andExpect(jsonPath("$.additionalData", is(updatePet.getAdditionalData())));

        mockMvc.perform(patch("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.type", is(updatePet.getType().toString())))
                .andExpect(jsonPath("$.name", is(updatePet.getName())))
                .andExpect(jsonPath("$.breed", is(updatePet.getBreed())))
                .andExpect(jsonPath("$.birthDate", is(updatePet.getBirthDate().format(formatter))))
                .andExpect(jsonPath("$.sex", is(updatePet.getSex().toString())))
                .andExpect(jsonPath("$.age", is(updatedPet.getAge())))
                .andExpect(jsonPath("$.color", is(updatePet.getColor())))
                .andExpect(jsonPath("$.sign", is(updatePet.getSign())))
                .andExpect(jsonPath("$.isExhibition", is(updatePet.getIsExhibition())))
                .andExpect(jsonPath("$.vetVisitDate", is(updatePet.getVetVisitDate().format(formatter))))
                .andExpect(jsonPath("$.vetVisitReason", is(updatePet.getVetVisitReason())))
                .andExpect(jsonPath("$.vaccine", is(updatePet.getVaccine())))
                .andExpect(jsonPath("$.parasites", is(updatePet.getParasites())))
                .andExpect(jsonPath("$.fleaMite", is(updatePet.getFleaMite())))
                .andExpect(jsonPath("$.surgery", is(updatePet.getSurgery())))
                .andExpect(jsonPath("$.pastDisease", is(updatePet.getPastDisease())))
                .andExpect(jsonPath("$.healthCharacteristic", is(updatePet.getHealthCharacteristic())))
                .andExpect(jsonPath("$.urineAnalysis", is(updatePet.getUrineAnalysis())))
                .andExpect(jsonPath("$.isAllergy", is(updatePet.getIsAllergy())))
                .andExpect(jsonPath("$.allergyType", is(updatePet.getAllergyType())))
                .andExpect(jsonPath("$.isChronicDisease", is(updatePet.getIsChronicDisease())))
                .andExpect(jsonPath("$.chronicDiseaseType", is(updatePet.getChronicDiseaseType())))
                .andExpect(jsonPath("$.heatDate", is(updatePet.getHeatDate().format(formatter))))
                .andExpect(jsonPath("$.vetData", is(updatePet.getVetData())))
                .andExpect(jsonPath("$.stayWithoutMaster", is(updatePet.getStayWithoutMaster())))
                .andExpect(jsonPath("$.stayAlone", is(updatePet.getStayAlone())))
                .andExpect(jsonPath("$.specialCare", is(updatePet.getSpecialCare())))
                .andExpect(jsonPath("$.barkHowl", is(updatePet.getBarkHowl())))
                .andExpect(jsonPath("$.furnitureDamage", is(updatePet.getFurnitureDamage())))
                .andExpect(jsonPath("$.foodFromTable", is(updatePet.getFoodFromTable())))
                .andExpect(jsonPath("$.defecateAtHome", is(updatePet.getDefecateAtHome())))
                .andExpect(jsonPath("$.markAtHome", is(updatePet.getMarkAtHome())))
                .andExpect(jsonPath("$.newPeople", is(updatePet.getNewPeople())))
                .andExpect(jsonPath("$.isBitePeople", is(updatePet.getIsBitePeople())))
                .andExpect(jsonPath("$.reasonOfBite", is(updatePet.getReasonOfBite())))
                .andExpect(jsonPath("$.playWithDogs", is(updatePet.getPlayWithDogs())))
                .andExpect(jsonPath("$.isDogTraining", is(updatePet.getIsDogTraining())))
                .andExpect(jsonPath("$.trainingName", is(updatePet.getTrainingName())))
                .andExpect(jsonPath("$.like", is(updatePet.getLike())))
                .andExpect(jsonPath("$.notLike", is(updatePet.getNotLike())))
                .andExpect(jsonPath("$.toys", is(updatePet.getToys())))
                .andExpect(jsonPath("$.badHabit", is(updatePet.getBadHabit())))
                .andExpect(jsonPath("$.walking", is(updatePet.getWalking())))
                .andExpect(jsonPath("$.morningWalking", is(updatePet.getMorningWalking())))
                .andExpect(jsonPath("$.dayWalking", is(updatePet.getDayWalking())))
                .andExpect(jsonPath("$.eveningWalking", is(updatePet.getEveningWalking())))
                .andExpect(jsonPath("$.feedingQuantity", is(updatePet.getFeedingQuantity())))
                .andExpect(jsonPath("$.feedType", is(updatePet.getFeedType())))
                .andExpect(jsonPath("$.feedName", is(updatePet.getFeedName())))
                .andExpect(jsonPath("$.feedComposition", is(updatePet.getFeedComposition())))
                .andExpect(jsonPath("$.feedingRate", is(updatePet.getFeedingRate())))
                .andExpect(jsonPath("$.feedingPractice", is(updatePet.getFeedingPractice())))
                .andExpect(jsonPath("$.treat", is(updatePet.getTreat())))
                .andExpect(jsonPath("$.isMedicine", is(updatePet.getIsMedicine())))
                .andExpect(jsonPath("$.medicineRegimen", is(updatePet.getMedicineRegimen())))
                .andExpect(jsonPath("$.additionalData", is(updatePet.getAdditionalData())));


        String errorAccessDenied = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());
        when(petService.updatePet(anyLong(), eq(petDto.getId()), any())).thenThrow(new AccessDeniedException(errorAccessDenied));

        mockMvc.perform(patch("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isForbidden());

        String errorNotFoundPet = String.format("Pet with id = %d not found", petDto.getId());
        when(petService.updatePet(anyLong(), eq(0L), any())).thenThrow(new NotFoundException(errorNotFoundPet));

        mockMvc.perform(patch("/pets/{id}", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(patch("/pets/{id}", petDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updatePet)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(petService, times(4)).updatePet(anyLong(), anyLong(), any(UpdatePetDto.class));
    }

    @Test
    @SneakyThrows
    void deletePet() {
        doNothing().when(petService).deletePetById(anyLong(), anyLong());

        mockMvc.perform(delete("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterAdmin.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        String errorNotFoundPet = String.format("Pet with id = %d not found", petDto.getId());
        doThrow(new NotFoundException(errorNotFoundPet)).when(petService).deletePetById(anyLong(), anyLong());

        mockMvc.perform(delete("/pets/{id}", 0L)
                        .header(requesterHeader, requesterBoss.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        String errorAccessDenied = String.format("User with role = %s, can't access for this action",
                requesterUser.getRole());
        doThrow(new AccessDeniedException(errorAccessDenied)).when(petService).deletePetById(anyLong(), anyLong());

        mockMvc.perform(delete("/pets/{id}", petDto.getId())
                        .header(requesterHeader, requesterUser.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/pets/{id}", petDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());


        verify(petService, times(4)).deletePetById(anyLong(), anyLong());
    }


}
