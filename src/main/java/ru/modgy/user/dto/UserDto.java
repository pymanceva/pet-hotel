package ru.modgy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.modgy.user.model.Roles;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String email;
    private String password;
    private Roles role;
    private Boolean isActive;
}
