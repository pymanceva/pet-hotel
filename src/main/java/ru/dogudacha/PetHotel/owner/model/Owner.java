package ru.dogudacha.PetHotel.owner.model;

import jakarta.persistence.*;
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
    @Column(name = "name_owners", nullable = false)
    private String name;
    @Column(name = "email_owners", nullable = false)
    private String email;
    @Column(name = "main_phone_owners", nullable = false)
    private String mainPhone;
    @Column(name = "optional_phone_owners")
    private String optionalPhone;
    @Column(name = "instagram_owners")
    private String instagram;
    @Column(name = "youtube_owners")
    private String youtube;
    @Column(name = "facebook_owners")
    private String facebook;
    @Column(name = "tiktok_owners")
    private String tiktok;
    @Column(name = "twitter_owners")
    private String twitter;
    @Column(name = "telegram_owners")
    private String telegram;
    @Column(name = "whatsapp_owners")
    private String whatsapp;
    @Column(name = "viber_owners")
    private String viber;
    @Column(name = "vk_owners")
    private String vk;
    @Column(name = "prefer_communication_owners", nullable = false)
    @Enumerated(EnumType.STRING)
    private MethodsOfCommunication preferCommunication;
    @Column(name = "car_registration_number_owners")
    private String carRegistrationNumber;
}
