package ru.dogudacha.PetHotel.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dogudacha.PetHotel.room.model.RoomTypes;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomWithoutPriceDto {
    private Long id;
    private String number;
    private RoomTypes type;
    private Double size;
    private Boolean isAvailable;
}
