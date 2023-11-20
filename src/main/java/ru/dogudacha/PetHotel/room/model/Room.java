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
    @Column(name = "id_room")
    private Long id;
    @Column(name = "number_room", nullable = false)
    private String number;
    @Column(name = "area_room")
    private Double area;
    @Column(name = "type_room", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomTypes type;
    @Column(name = "description_room")
    private String description;
    @Column(name = "visible_room")
    private Boolean isVisible;
}
