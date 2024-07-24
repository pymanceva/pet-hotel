package ru.modgy.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.modgy.user.model.Roles;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {
    @Size(min = 2, max = 30, message = "Длинна фамилии должна быть между {min} и {max}.")
    @Pattern(regexp = "^(?=.*[a-zA-Zа-яёЁА-Я\\d_\\S]).+$")
    private String lastName;

    @NotBlank(message = "Имя не может быть пустым.")
    @Size(min = 2, max = 15, message = "Длинна имени должна быть между {min} и {max}.")
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d_\\S]).+$")
    private String firstName;

    @Size(min = 2, max = 15, message = "Длинна отчества должна быть между {min} и {max}.")
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d_\\S]).+$")
    private String middleName;

    @Size(min = 5, max = 10, message = "Длинна пароля должна быть между {min} и {max}.")
    @Pattern(regexp = "^(?=.*[a-zA-Z\\d_\\S]).+(\\S)$")
    private String password;

    @Email(message = "Неверный формат email.")
    @NotBlank(message = "Email Имя не может быть пустым.")
    @Size(min = 6, max = 254, message = "Длинна email должна быть между {min} и {max}.")
    private String email;

    @NotNull(message = "Role не может быть пустым.")
    private Roles role;

    @Builder.Default
    private Boolean isActive = true;
}
