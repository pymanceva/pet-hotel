package ru.modgy.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.modgy.user.model.Roles;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    @Size(min = 2, max = 30, message = "Длина фамилии должна быть между {min} и {max}.")
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d_\\S]).+$")
    private String lastName;

    @Size(min = 2, max = 15, message = "Длина имени должна быть между {min} и {max}.")
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d_\\S]).+$")
    private String firstName;

    @Size(min = 2, max = 15, message = "Длина отчества должна быть между {min} и {max}.")
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d_\\S]).+$")
    private String middleName;

    @Size(min = 5, max = 10, message = "Длина пароля должна быть между {min} и {max}.")
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d_\\S]).+(\\S)$", message = "Неподходящие символы в пароле. (я ленивая жопа)")
    private String password;

    @Email(message = "Неверный формат email.")
    @Size(min = 6, max = 254, message = "Длина email должна быть между {min} и {max}.")
    private String email;

    private Roles role;
}
