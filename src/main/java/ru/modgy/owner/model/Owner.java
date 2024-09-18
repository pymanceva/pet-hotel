package ru.modgy.owner.model;

import jakarta.persistence.*;
import lombok.*;
import ru.modgy.pet.model.Pet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "id_owners")
    private Long id;
    @Column(name = "last_name_owners")
    private String lastName;
    @Column(name = "first_name_owners", nullable = false)
    private String firstName;
    @Column(name = "middle_name_owners")
    private String middleName;
    @Column(name = "main_phone_owners", nullable = false)
    private String mainPhone;
    @Column(name = "optional_phone_owners")
    private String optionalPhone;
    @Column(name = "other_contacts_owners")
    private String otherContacts;
    @Column(name = "actual_address_owners")
    private String actualAddress;
    @Column(name = "trusted_man_owners")
    private String trustedMan;
    @Column(name = "source_owners")
    private String source;
    @Column(name = "comment_owners")
    private String comment;
    @Column(name = "rating_owners")
    private Integer rating;
    @Column(name = "registration_date_owners")
    private LocalDateTime registrationDate;
    @OneToMany(mappedBy = "owner",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<Pet> pets = new ArrayList<>();
}
