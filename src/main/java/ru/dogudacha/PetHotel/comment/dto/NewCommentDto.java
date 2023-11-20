package ru.dogudacha.PetHotel.comment.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    @NotBlank(message = "Field: text. Error: must not be blank.")
    @Length(min = 1, max = 1024, message = "Field: text. Error: must not be more than 1024 characters.")
    private String text;
}
