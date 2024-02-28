package ru.modgy.room.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Field: name. Error: must not be blank.")
    @Size(min = 1, max = 20)
    private String name;
    @Size(max = 250)
    private String description;
}
