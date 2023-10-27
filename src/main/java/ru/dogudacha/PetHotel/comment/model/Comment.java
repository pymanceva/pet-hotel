package ru.dogudacha.PetHotel.comment.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import ru.dogudacha.PetHotel.pet.model.Pet;
import ru.dogudacha.PetHotel.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text", nullable = false, length = 1024)
    private String text;
    @ManyToOne()
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
    @ManyToOne()
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @CreationTimestamp
    private LocalDateTime created;
}
