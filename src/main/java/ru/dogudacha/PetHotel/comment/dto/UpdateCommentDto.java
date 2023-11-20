package ru.dogudacha.PetHotel.comment.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDto {
    @NotBlank(message = "Field: text. Error: must not be blank.")
    @Length(min = 1, max = 1024, message = "Field: text. Error: must not be more than 1024 characters.")
    private String text;
}
