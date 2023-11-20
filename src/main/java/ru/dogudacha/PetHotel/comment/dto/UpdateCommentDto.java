package ru.dogudacha.PetHotel.comment.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDto {
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Length(min = 1, max = 1024, message = "Длина текста комментария должна быть от 1 до 1024 символов")
    private String text;
}
