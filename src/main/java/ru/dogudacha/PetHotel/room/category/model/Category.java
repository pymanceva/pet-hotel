package ru.dogudacha.PetHotel.room.category.model;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categories")
    private Long id;
    @Column(name = "name_categories", nullable = false)
    private String name;
    @Column(name = "description_categories")
    private String description;
}
