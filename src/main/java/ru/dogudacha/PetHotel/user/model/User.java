package ru.dogudacha.PetHotel.user.model;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "last_name_users")
    private String lastName;
    @Column(name = "first_name_users", nullable = false)
    private String firstName;
    @Column(name = "middle_name_users")
    private String middleName;
    @Column(name = "email_users", nullable = false)
    private String email;
    @Column(name = "password_users")
    private String password;
    @Column(name = "role_users", nullable = false)
    @Enumerated(EnumType.STRING)
    Roles role;
    @Column(name = "active_users", nullable = false)
    private Boolean isActive;
}
