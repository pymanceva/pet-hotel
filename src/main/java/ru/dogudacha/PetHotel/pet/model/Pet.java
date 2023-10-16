package ru.dogudacha.PetHotel.pet.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@Entity
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
    private int age;
    @Column(name = "weight_pet", nullable = false)
    private int weight;
    @Column(name = "diet_pet", nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeOfDiet diet;
    @Column(name = "medication_pet", nullable = false)
    private boolean isTakesMedications;
    @Column(name = "contact_pet", nullable = false)
    private boolean isContact; //гуляет ли с другими животными
    @Column(name = "photographed_pet", nullable = false)
    private boolean isPhotographed; // согласие владельца на фото и видео
    @Column(name = "comments_pet", nullable = false, length = 1000)
    private String comments;

}
