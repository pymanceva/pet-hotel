package ru.dogudacha.PetHotel.pet.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfDiet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class PetDto {
    private long id;
    private String typeOfPet; // вид животного
    private String breed; // порода
    private Sex sex;
    private Integer age;
    private Integer weight;
    private TypeOfDiet diet;
    private Boolean isTakesMedications;
    private Boolean isContact; //гуляет ли с другими животными
    private Boolean isPhotographed; // согласие владельца на фото и видео
    private String comments;
}
