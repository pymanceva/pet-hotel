package ru.modgy.pet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.modgy.pet.model.Sex;
import ru.modgy.pet.model.TypeOfPet;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PetDto {
    private long id;
//    private long ownerId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private TypeOfPet type; // вид животного
    private String name;
    private String breed; // порода
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
    private String age;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Sex sex;
    private String color;
    private String sign;
    private Boolean isExhibition;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate vetVisitDate;
    private String vetVisitReason;
    private String vaccine;
    private String parasites;
    private String fleaMite;
    private String surgery;
    private String pastDisease;
    private String healthCharacteristic;
    private String urineAnalysis;
    private Boolean isAllergy;
    private String allergyType;
    private Boolean isChronicDisease;
    private String chronicDiseaseType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate heatDate;
    private String vetData;
    private String stayWithoutMaster;
    private String stayAlone;
    private String specialCare;
    private String barkHowl;
    private String furnitureDamage;
    private String foodFromTable;
    private String defecateAtHome;
    private String markAtHome;
    private String newPeople;
    private Boolean isBitePeople;
    private String reasonOfBite;
    private String playWithDogs;
    private Boolean isDogTraining;
    private String trainingName;
    private String like;
    private String notLike;
    private String toys;
    private String badHabit;
    private String walking;
    private String morningWalking;
    private String dayWalking;
    private String eveningWalking;
    private Integer feedingQuantity;
    private String feedType;
    private String feedName;
    private String feedComposition;
    private String feedingRate;
    private String feedingPractice;
    private String treat;
    private Boolean isMedicine;
    private String medicineRegimen;
    private String additionalData;
}