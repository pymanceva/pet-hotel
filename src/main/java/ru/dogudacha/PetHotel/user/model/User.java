package ru.dogudacha.PetHotel.user.model;

import jakarta.persistence.*;
import lombok.*;
import ru.dogudacha.PetHotel.pet.model.Pet;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    Roles role;
//
//    @OneToMany(mappedBy = "pet",
//            orphanRemoval = true)
//    private List<Pet> pets = new ArrayList<>();
}
