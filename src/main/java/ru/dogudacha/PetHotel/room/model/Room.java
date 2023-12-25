package ru.dogudacha.PetHotel.room.model;

import jakarta.persistence.*;
import lombok.*;
import ru.dogudacha.PetHotel.room.category.model.Category;

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
    @Column(name = "id_rooms")
    private Long id;
    @Column(name = "number_rooms", nullable = false)
    private String number;
    @Column(name = "area_rooms")
    private Double area;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id_rooms")
    @ToString.Exclude
    private Category category;
    @Column(name = "description_rooms")
    private String description;
    @Column(name = "visible_rooms")
    private Boolean isVisible;
}
