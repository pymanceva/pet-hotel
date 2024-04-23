package ru.modgy.pet.model;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "id_pets")
    private long id;
    //    @ManyToOne()
//    @JoinColumn(name = "owner_id_pet", nullable = false)
//    @Column(name = "owner_id_pet", nullable = false)
//    private long owner;
    @Column(name = "type_pets", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfPet type;
    @Column(name = "name_pets", nullable = false, length = 30)
    private String name;
    @Column(name = "breed_pets", nullable = false, length = 30)
    private String breed; // порода
    @Column(name = "birth_date_pets", nullable = false)
    private LocalDate birthDate;
    @Column(name = "sex_pets", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(name = "color_pets", nullable = true, length = 30)
    private String color;
    @Column(name = "sign_pets", nullable = true, length = 150)
    private String sign; //идентификация (чип, клеймо)/особые приметы
    @Column(name = "exhibition_pets", nullable = true)
    private Boolean isExhibition;
    @Column(name = "vet_visit_pets", nullable = true)
    private LocalDate vetVisitDate;
    @Column(name = "reason_vet_visit_pets", nullable = true, length = 250)
    private String vetVisitReason;
    @Column(name = "vaccine_pets", nullable = true, length = 250)
    private String vaccine;
    @Column(name = "parasites_pets", nullable = true, length = 250)
    private String parasites;
    @Column(name = "flea_mite_pets", nullable = true, length = 250)
    private String fleaMite;
    @Column(name = "surgery_pets", nullable = true, length = 250)
    private String surgery;
    @Column(name = "past_disease_pets", nullable = true, length = 500)
    private String pastDisease;
    @Column(name = "health_characteristic_pets", nullable = true, length = 500)
    private String healthCharacteristic;
    @Column(name = "urine_analysis_pets", nullable = true, length = 250)
    private String urineAnalysis;
    @Column(name = "allergy_pets", nullable = true)
    private Boolean isAllergy;
    @Column(name = "allergy_type_pets", nullable = true, length = 250)
    private String allergyType;
    @Column(name = "chronic_disease_pets", nullable = true)
    private Boolean isChronicDisease;
    @Column(name = "chronic_disease_type_pets", nullable = true, length = 500)
    private String chronicDiseaseType;
    @Column(name = "heat_date_pets", nullable = true)
    private LocalDate heatDate;
    @Column(name = "vet_data_pets", nullable = true, length = 500)
    private String vetData;
    @Column(name = "stay_without_master_pets", nullable = true, length = 500)
    private String stayWithoutMaster;
    @Column(name = "stay_alone_pets", nullable = true, length = 250)
    private String stayAlone;
    @Column(name = "special_care_pets", nullable = true, length = 500)
    private String specialCare;
    @Column(name = "bark_howl_pets", nullable = true, length = 250)
    private String barkHowl;
    @Column(name = "furniture_damage_pets", nullable = true, length = 250)
    private String furnitureDamage;
    @Column(name = "food_from_table_pets", nullable = true, length = 250)
    private String foodFromTable;
    @Column(name = "defecate_at_home_pets", nullable = true, length = 250)
    private String defecateAtHome;
    @Column(name = "mark_at_home_pets", nullable = true, length = 250)
    private String markAtHome;
    @Column(name = "new_people_pets", nullable = true, length = 500)
    private String newPeople;
    @Column(name = "bite_people_pets", nullable = true)
    private Boolean isBitePeople;
    @Column(name = "reason_of_bite_pets", nullable = true, length = 250)
    private String reasonOfBite;
    @Column(name = "play_with_dogs_pets", nullable = true, length = 500)
    private String playWithDogs;
    @Column(name = "dog_training_pets", nullable = true)
    private Boolean isDogTraining;
    @Column(name = "training_name_pets", nullable = true, length = 500)
    private String trainingName;
    @Column(name = "like_pets", nullable = true, length = 500)
    private String like;
    @Column(name = "not_like_pets", nullable = true, length = 500)
    private String notLike;
    @Column(name = "toys_pets", nullable = true, length = 500)
    private String toys;
    @Column(name = "bad_habit_pets", nullable = true, length = 250)
    private String badHabit;
    @Column(name = "walking_pets", nullable = true, length = 250)
    private String walking;
    @Column(name = "morning_walking_pets", nullable = true, length = 150)
    private String morningWalking;
    @Column(name = "day_walking_pets", nullable = true, length = 150)
    private String dayWalking;
    @Column(name = "evening_walking_pets", nullable = true, length = 150)
    private String eveningWalking;
    @Column(name = "feeding_quantity_pets", nullable = true)
    private Integer feedingQuantity;
    @Column(name = "feed_type_pets", nullable = true, length = 250)
    private String feedType;
    @Column(name = "feed_name_pets", nullable = true, length = 250)
    private String feedName;
    @Column(name = "feed_composition_pets", nullable = true, length = 250)
    private String feedComposition;
    @Column(name = "feeding_rate_pets", nullable = true, length = 250)
    private String feedingRate;
    @Column(name = "feeding_practice_pets", nullable = true, length = 500)
    private String feedingPractice;
    @Column(name = "treat_pets", nullable = true, length = 250)
    private String treat;
    @Column(name = "medicine_pets", nullable = true)
    private Boolean isMedicine;
    @Column(name = "medicine_regimen_pets", nullable = true, length = 500)
    private String medicineRegimen;
    @Column(name = "additional_data_pets", nullable = true, length = 1000)
    private String additionalData;
}



