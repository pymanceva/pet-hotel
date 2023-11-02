package ru.dogudacha.PetHotel.room.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "number", nullable = false)
    private String number;
    @Column(name = "size")
    private Double size;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomTypes type;
    @Column(name = "price")
    private Double price;
    @Column(name = "available")
    private Boolean isAvailable;
}
