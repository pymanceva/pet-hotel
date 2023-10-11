package ru.dogudacha.PetHotel.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dogudacha.PetHotel.user.model.Roles;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Field: name. Error: must not be blank.")
    @Size(min = 2, max = 250, message = "validation name size error")
    private String name;
    @Email(message = "Field: email. Error: must be email format.")
    @NotBlank(message = "Field: email. Error: must not be blank.")
    @Size(min = 6, max = 254, message = "validation email size error")
    private String email;
    @NotNull(message = "Field: role. Error: must not be null.")
    private Roles role;
}
