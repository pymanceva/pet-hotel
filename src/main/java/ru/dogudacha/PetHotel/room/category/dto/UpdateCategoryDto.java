package ru.dogudacha.PetHotel.room.category.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryDto {
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d_\\S]).+$")
    private String name;
    @Size(max = 250)
    private String description;
}
