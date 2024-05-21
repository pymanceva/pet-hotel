package ru.modgy.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerShortDto {
    private Long id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String mainPhone;
    private String optionalPhone;
    private LocalDateTime registrationDate;
}
