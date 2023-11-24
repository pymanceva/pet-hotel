package ru.dogudacha.PetHotel.room.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dogudacha.PetHotel.room.model.RoomTypes;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Long id;
    @NotBlank(message = "Field: number. Error: must not be blank.")
    @Size(min = 1, max = 100)
    private String number;
    @Min(value = 0, message = "Field: area. Error: must not be negative.")
    private Double area;
    @NotNull(message = "Field: type. Error: must not be null.")
    private RoomTypes type;
    private String description;
    private Boolean isVisible;
}
