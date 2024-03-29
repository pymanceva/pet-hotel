package ru.modgy.pet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.modgy.pet.model.Sex;
import ru.modgy.pet.model.TypeOfPet;

import java.time.LocalDate;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePetDto {
    private TypeOfPet type; // вид животного
    @Length(max = 30, message = "validation name size error")
    private String name;
    @Length(max = 30, message = "validation breed size error")
    private String breed; // порода
    @Past(message = "Field: birthDate. Error: must be past.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate birthDate;
    private Sex sex;
    @Length(max = 30, message = "validation color size error")
    private String color;
    @Length(max = 150, message = "validation sign size error")
    private String sign;
    private Boolean isExhibition;
    @PastOrPresent(message = "Field: vetVisitDate. Error: must be past or present.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate vetVisitDate;
    @Length(max = 250, message = "validation vetVisitReason size error")
    private String vetVisitReason;
    @Length(max = 250, message = "validation vaccine size error")
    private String vaccine;
    @Length(max = 250, message = "validation parasites size error")
    private String parasites;
    @Length(max = 250, message = "validation fleaMite size error")
    private String fleaMite;
    @Length(max = 250, message = "validation surgery size error")
    private String surgery;
    @Length(max = 500, message = "validation pastDisease size error")
    private String pastDisease;
    @Length(max = 500, message = "validation healthCharacteristic size error")
    private String healthCharacteristic;
    @Length(max = 250, message = "validation urineAnalysis size error")
    private String urineAnalysis;
    private Boolean isAllergy;
    @Length(max = 250, message = "validation allergyType size error")
    private String allergyType;
    private Boolean isChronicDisease;
    @Length(max = 500, message = "validation chronicDiseaseType size error")
    private String chronicDiseaseType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate heatDate;
    @Length(max = 500, message = "validation vetData size error")
    private String vetData;
    @Length(max = 500, message = "validation stayWithoutMaster size error")
    private String stayWithoutMaster;
    @Length(max = 250, message = "validation stayAlone size error")
    private String stayAlone;
    @Length(max = 500, message = "validation specialCare size error")
    private String specialCare;
    @Length(max = 250, message = "validation barkHowl size error")
    private String barkHowl;
    @Length(max = 250, message = "validation furnitureDamage size error")
    private String furnitureDamage;
    @Length(max = 250, message = "validation foodFromTable size error")
    private String foodFromTable;
    @Length(max = 250, message = "validation defecateAtHome size error")
    private String defecateAtHome;
    @Length(max = 250, message = "validation markAtHome size error")
    private String markAtHome;
    @Length(max = 500, message = "validation newPeople size error")
    private String newPeople;
    private Boolean isBitePeople;
    @Length(max = 250, message = "validation reasonOfBite size error")
    private String reasonOfBite;
    @Length(max = 500, message = "validation playWithDogs size error")
    private String playWithDogs;
    private Boolean isDogTraining;
    @Length(max = 500, message = "validation trainingName size error")
    private String trainingName;
    @Length(max = 500, message = "validation like size error")
    private String like;
    @Length(max = 500, message = "validation notLike size error")
    private String notLike;
    @Length(max = 500, message = "validation toys size error")
    private String toys;
    @Length(max = 250, message = "validation badHabit size error")
    private String badHabit;
    @Length(max = 250, message = "validation walking size error")
    private String walking;
    @Length(max = 150, message = "validation morningWalking size error")
    private String morningWalking;
    @Length(max = 150, message = "validation dayWalking size error")
    private String dayWalking;
    @Length(max = 150, message = "validation eveningWalking size error")
    private String eveningWalking;
    @Min(1)
    @Max(9)
    private Integer feedingQuantity;
    @Length(max = 250, message = "validation feedType size error")
    private String feedType;
    @Length(max = 250, message = "validation feedName size error")
    private String feedName;
    @Length(max = 250, message = "validation feedComposition size error")
    private String feedComposition;
    @Length(max = 250, message = "validation feedingRate size error")
    private String feedingRate;
    @Length(max = 500, message = "validation feedingPractice size error")
    private String feedingPractice;
    @Length(max = 250, message = "validation treat size error")
    private String treat;
    private Boolean isMedicine;
    @Length(max = 500, message = "validation medicineRegimen size error")
    private String medicineRegimen;
    @Length(max = 1000, message = "validation additionalData size error")
    private String additionalData;
}
