package ru.modgy.room.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewRoomDto {
    @Size(min = 1, max = 100)
    @NotBlank(message = "Поле: number. Error: must not be blank.")
    private String number;
    @Min(value = 0, message = "Поле: площадь. Ошибка: должно быть не меньше {value}.")
    private Double area;
    @NotNull
    private Long categoryId;
    @Size(max = 250)
    private String description;
    private Boolean isVisible;
}
