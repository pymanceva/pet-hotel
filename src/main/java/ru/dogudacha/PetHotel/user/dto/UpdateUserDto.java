package ru.dogudacha.PetHotel.user.dto;

import jakarta.validation.constraints.Email;
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
public class UpdateUserDto {
    private Long id;

    @Size(min = 2, max = 30, message = "validation name size error")
    private String lastName;

    @Size(min = 2, max = 15, message = "validation name size error")
    private String firstName;

    @Size(min = 2, max = 15, message = "validation name size error")
    private String middleName;

    @Size(min = 5, max = 10, message = "validation password size error")
    private String password;

    @Email(message = "Field: email. Error: must be email format.")
    @Size(min = 6, max = 254, message = "validation email size error")
    private String email;

    private Roles role;
}
