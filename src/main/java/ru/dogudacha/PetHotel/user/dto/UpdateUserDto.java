package ru.dogudacha.PetHotel.user.dto;

import jakarta.annotation.Nullable;
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
    @Nullable
    private Long id;
    @Nullable
    @Size(min = 2, max = 250, message = "validation name size error")
    private String name;
    @Nullable
    @Email(message = "Field: email. Error: must be email format.")
    @Size(min = 6, max = 254, message = "validation email size error")
    private String email;
    @Nullable
    private Roles role;
}
