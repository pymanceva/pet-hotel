package ru.dogudacha.PetHotel.pet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.dogudacha.PetHotel.pet.model.Sex;
import ru.dogudacha.PetHotel.pet.model.TypeOfDiet;

@Getter
@Setter
@Builder
@ToString
public class NewPetDto {
    @Length(max = 250)
    @NotBlank(message = "Field: typeOfPet. Error: must not be blank.")
    private String typeOfPet; // вид животного
    @Length(max = 250)
    @NotBlank(message = "Field: breed. Error: must not be blank.")
    private String breed; // порода
    @NotNull(message = "Field: sex. Error: must not be null.")
    private Sex sex;
    @NotNull(message = "Field: age. Error: must not be null.")
    private Integer age;
    @NotNull(message = "Field: weight. Error: must not be null.")
    private Integer weight;
    @NotNull(message = "Field: diet. Error: must not be null.")
    private TypeOfDiet diet;
    @NotNull(message = "Field: isTakesMedications. Error: must not be null.")
    private Boolean isTakesMedications;
    @NotNull(message = "Field: isContact. Error: must not be null.")
    private Boolean isContact; //гуляет ли с другими животными
    @NotNull(message = "Field: isPhotographed. Error: must not be null.")
    private Boolean isPhotographed; // согласие владельца на фото и видео
    @Length(max = 1000)
    private String comments;
}
