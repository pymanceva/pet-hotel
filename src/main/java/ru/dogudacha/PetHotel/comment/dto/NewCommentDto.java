package ru.dogudacha.PetHotel.comment.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    @NotBlank(message = "Field: text. Error: must not be blank.")
    private String text;
}
