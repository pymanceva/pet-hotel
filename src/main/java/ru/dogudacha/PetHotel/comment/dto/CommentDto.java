package ru.dogudacha.PetHotel.comment.dto;

import lombok.*;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private PetDto pet;
    private UserDto author;
    private LocalDateTime created;
}
