package ru.dogudacha.PetHotel.owner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "owners")
@NoArgsConstructor
@AllArgsConstructor
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "main_phone", nullable = false)
    private String mainPhone;
    @Column(name = "optional_phone")
    private String optionalPhone;
    @Column(name = "instagram")
    private String instagram;
    @Column(name = "youtube")
    private String youtube;
    @Column(name = "facebook")
    private String facebook;
    @Column(name = "tiktok")
    private String tiktok;
    @Column(name = "twitter")
    private String twitter;
    @Column(name = "telegram")
    private String telegram;
    @Column(name = "whatsapp")
    private String whatsapp;
    @Column(name = "viber")
    private String viber;
    @Column(name = "vk")
    private String vk;
    @Column(name = "prefer_communication", nullable = false)
    @Enumerated(EnumType.STRING)
    private MethodsOfCommunication preferCommunication;
    @Column(name = "car_registration_number")
    private String carRegistrationNumber;
}
