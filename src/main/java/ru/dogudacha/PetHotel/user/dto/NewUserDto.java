package ru.dogudacha.PetHotel.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dogudacha.PetHotel.user.model.Roles;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {
    @NotBlank(message = "Field: lastName. Error: must not be blank.")
    @Size(min = 2, max = 30, message = "validation name size error")
    private String lastName;

    @NotBlank(message = "Field: firstName. Error: must not be blank.")
    @Size(min = 2, max = 15, message = "validation name size error")
    private String firstName;

    @NotBlank(message = "Field: middleName. Error: must not be blank.")
    @Size(min = 2, max = 15, message = "validation name size error")
    private String middleName;

    @Size(min = 5, max = 10, message = "validation password size error")
    private String password;

    @Email(message = "Field: email. Error: must be email format.")
    @NotBlank(message = "Field: email. Error: must not be blank.")
    @Size(min = 6, max = 254, message = "validation email size error")
    private String email;

    @NotNull(message = "Field: role. Error: must not be null.")
    private Roles role;

    private Boolean isActive;
}
