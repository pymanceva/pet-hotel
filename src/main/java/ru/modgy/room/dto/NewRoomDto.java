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
    @NotBlank(message = "Field: number. Error: must not be blank.")
    private String number;
    @Min(value = 0, message = "Field: area. Error: must not be negative.")
    private Double area;
    @NotNull
    private Long categoryId;
    @Size(max = 250)
    private String description;
    private Boolean isVisible;
}
