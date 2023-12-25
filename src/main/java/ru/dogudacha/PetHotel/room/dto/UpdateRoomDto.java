package ru.dogudacha.PetHotel.room.dto;

import jakarta.validation.constraints.Min;
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
public class UpdateRoomDto {
    @Size(max = 100)
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d_\\S]).+$")
    private String number;
    @Min(value = 0, message = "Field: area. Error: must not be negative.")
    private Double area;
    private Long categoryId;
    @Size(max = 250)
    private String description;
}
