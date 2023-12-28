package ru.dogudacha.PetHotel.user.dto;

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
    private String lastName;
    private String firstName;
    private String middleName;
    private String email;
    private String password;
    private Roles role;
    private Boolean isActive;
}
