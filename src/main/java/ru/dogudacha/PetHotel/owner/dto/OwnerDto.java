package ru.dogudacha.PetHotel.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dogudacha.PetHotel.owner.model.MethodsOfCommunication;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerDto {
    private Long id;
    private String name;
    private String email;
    private String mainPhone;
    private String optionalPhone;
    private String instagram;
    private String youtube;
    private String facebook;
    private String tiktok;
    private String twitter;
    private String telegram;
    private String whatsapp;
    private String viber;
    private String vk;
    private MethodsOfCommunication preferCommunication;
    private String carRegistrationNumber;
}
