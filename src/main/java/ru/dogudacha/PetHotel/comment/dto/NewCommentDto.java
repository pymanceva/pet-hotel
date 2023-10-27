package ru.dogudacha.PetHotel.comment.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
public class NewCommentDto {
    @NotBlank(message = "Текст комментария не может быть пустым")
    @Length(min = 1, max = 1024, message = "Длина текста комментария должна быть от 1 до 1024 символов")
    private String text;
    @NotNull
    private Long pet;
}
