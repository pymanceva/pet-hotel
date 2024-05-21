package ru.modgy.owner.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOwnerDto {
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = """
                    Field: mainPhoneNumber. Error: wrong phone format. Right examples:
                    89261234567
                    79261234567
                    +7 926 123 45 67
                    8(926)123-45-67
                    123-45-67
                    9261234567
                    79261234567
                    (495)1234567
                    (495) 123 45 67
                    89261234567
                    8-926-123-45-67
                    8 927 1234 234
                    8 927 12 12 888
                    8 927 12 555 12
                    8 927 123 8 123
                    """)
    private String phone;
}
