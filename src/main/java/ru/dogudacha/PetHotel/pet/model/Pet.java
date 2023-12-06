package ru.dogudacha.PetHotel.pet.model;

import jakarta.persistence.*;
import lombok.*;
import ru.dogudacha.PetHotel.user.model.User;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@Entity
@ToString
@Table(name = "pets")
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    @ManyToOne()
//    @JoinColumn(name = "owner_id_pet", nullable = false)
    @Column(name = "owner_id_pet", nullable = false)
    private long owner;
    @Column(name = "type_pet", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfPet typeOfPet;
    @Column(name = "name_pet", nullable = false, length = 30)
    private String name;
    @Column(name = "breed_pet", nullable = false, length = 30)
    private String breed; // порода
    @Column(name = "birth_date_pet", nullable = true)
    private LocalDate birthDate;
    @Column(name = "age_pet", nullable = false, length = 10)
    private String age;
    @Column(name = "sex_pet", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(name = "color_pet", nullable = true, length = 30)
    private String color;
    @Column(name = "sign_pet", nullable = true, length = 150)
    private String sign; //идентификация (чип, клеймо)/особые приметы
    @Column(name = "is_exhibition_pet", nullable = true)
    private Boolean isExhibition;
    @Column(name = "vet_visit_pet", nullable = true)
    private LocalDate vetVisit;
    @Column(name = "reason_vet_visit_pet", nullable = true, length = 250)
    private String vetVisitReason;
    @Column(name = "vaccine_pet", nullable = true, length = 250)
    private String vaccine;
    @Column(name = "parasites_pet", nullable = true, length = 250)
    private String parasites;
    @Column(name = "flea_mite_pet", nullable = true, length = 250)
    private String fleaMite;
    @Column(name = "surgery_pet", nullable = true, length = 250)
    private String surgery;
    @Column(name = "past_disease_pet", nullable = true, length = 500)
    private String pastDisease;
    @Column(name = "health_characteristic_pet", nullable = true, length = 500)
    private String healthCharacteristic;
    @Column(name = "urine_analysis_pet", nullable = true, length = 250)
    private String urineAnalysis;
    @Column(name = "is_allergy_pet", nullable = true)
    private Boolean allergy;
    @Column(name = "allergy_type_pet", nullable = true, length = 250)
    private String allergyType;
    @Column(name = "is_chronic_disease_pet", nullable = true)
    private Boolean chronicDisease;
    @Column(name = "chronic_disease_type_pet", nullable = true, length = 500)
    private String chronicDiseaseType;
    @Column(name = "heat_date_pet", nullable = true)
    private LocalDate heatDate;
    @Column(name = "vet_data_pet", nullable = true, length = 500)
    private String vetData;
    @Column(name = "stay_without_master_pet", nullable = true, length = 500)
    private String stayWithoutMaster;
    @Column(name = "stay_alone_pet", nullable = true, length = 250)
    private String stayAlone;
    @Column(name = "special_care_pet", nullable = true, length = 500)
    private String specialCare;
    @Column(name = "bark_howl_pet", nullable = true, length = 250)
    private String barkHowl;
    @Column(name = "furniture_damage_pet", nullable = true, length = 250)
    private String furnitureDamage;
    @Column(name = "food_from_table_pet", nullable = true, length = 250)
    private String foodFromTable;
    @Column(name = "defecate_at_home_pet", nullable = true, length = 250)
    private String defecateAtHome;
    @Column(name = "mark_at_home_pet", nullable = true, length = 250)
    private String markAtHome;
    @Column(name = "new_people_pet", nullable = true, length = 500)
    private String newPeople;
    @Column(name = "is_bite_people_pet", nullable = true)
    private Boolean isBitePeople;
    @Column(name = "reason_of_bite_pet", nullable = true, length = 250)
    private String reasonOfBite;
    @Column(name = "play_with_dogs_pet", nullable = true, length = 500)
    private String playWithDogs;
    @Column(name = "is_dog_training_pet", nullable = true)
    private Boolean isDogTraining;
    @Column(name = "training_name_pet", nullable = true, length = 500)
    private String trainingName;
    @Column(name = "like_pet", nullable = true, length = 500)
    private String like;
    @Column(name = "no_like_pet", nullable = true, length = 500)
    private String notLike;
    @Column(name = "toys_pet", nullable = true, length = 500)
    private String toys;
    @Column(name = "bad_habit_pet", nullable = true, length = 250)
    private String badHabit;
    @Column(name = "walking_pet", nullable = true, length = 250)
    private String walking;
    @Column(name = "morning_walking_pet", nullable = true, length = 150)
    private String morningWalking;
    @Column(name = "day_walking_pet", nullable = true, length = 150)
    private String dayWalking;
    @Column(name = "evening_walking_pet", nullable = true, length = 150)
    private String eveningWalking;
    @Column(name = "feeding_quantity_pet", nullable = true)
    private Integer feedingQuantity;
    @Column(name = "feed_type_pet", nullable = true, length = 250)
    private String feedType;
    @Column(name = "feed_name_pet", nullable = true, length = 250)
    private String feedName;
    @Column(name = "feed_composition_pet", nullable = true, length = 250)
    private String feedComposition;
    @Column(name = "feeding_rate_pet", nullable = true, length = 250)
    private String feedingRate;
    @Column(name = "feeding_practice_pet", nullable = true, length = 500)
    private String feedingPractice;
    @Column(name = "treat_pet", nullable = true, length = 250)
    private String treat;
    @Column(name = "is_medicine_pet", nullable = true)
    private Boolean isMedicine;
    @Column(name = "medicine_regimen_pet", nullable = true, length = 500)
    private String medicineRegimen;
    @Column(name = "additional_data_pet", nullable = true, length = 1000)
    private String additionalData;
}



