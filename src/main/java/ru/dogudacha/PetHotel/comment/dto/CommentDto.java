package ru.dogudacha.PetHotel.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.dogudacha.PetHotel.pet.dto.PetDto;
import ru.dogudacha.PetHotel.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private PetDto pet;
    private UserDto author;
    private LocalDateTime created;
}
