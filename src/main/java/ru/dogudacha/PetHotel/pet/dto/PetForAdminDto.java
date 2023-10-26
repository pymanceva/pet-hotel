package ru.dogudacha.PetHotel.pet.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
public class PetForAdminDto extends PetDto {
    private String historyOfBookings; // история посещений
    private String additionalServices; // дополнительные услуги
}

