package ru.dogudacha.PetHotel.room.dto;

import jakarta.validation.constraints.Min;
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
public class UpdateRoomDto {
    private Long id;
    @Size(min = 1, max = 100)
    private String number;
    @Min(value = 0, message = "Field: area. Error: must not be negative.")
    private Double area;
    private RoomTypes type;
    private String description;
}
