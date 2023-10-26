package ru.dogudacha.PetHotel.pet.model;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "type_pet", nullable = false, length = 250)
    private String typeOfPet; // вид животного
    @Column(name = "breed_pet", nullable = false, length = 250)
    private String breed; // порода
    @Column(name = "sex_pet", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(name = "age_pet", nullable = false)
    private Integer age;
    @Column(name = "weight_pet", nullable = false)
    private Integer weight;
    @Column(name = "diet_pet", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfDiet diet;
    @Column(name = "medication_pet", nullable = false)
    private Boolean isTakesMedications;
    @Column(name = "contact_pet", nullable = false)
    private Boolean isContact; //гуляет ли с другими животными
    @Column(name = "photographed_pet", nullable = false)
    private Boolean isPhotographed; // согласие владельца на фото и видео
    @Column(name = "comments_pet", nullable = false, length = 1000)
    private String comments;
}
