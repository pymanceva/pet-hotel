package ru.dogudacha.PetHotel.owner.dto;

import ch.qos.logback.classic.pattern.MethodOfCallerConverter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dogudacha.PetHotel.owner.model.MethodsOfCommunication;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOwnerDto {
    @NotBlank(message = "Field: name. Error: must not be blank.")
    @Size(min = 2, max = 250, message = "validation name size error")
    private String name;
    @Email(message = "Field: email. Error: must be email format.")
    @NotBlank(message = "Field: email. Error: must not be blank.")
    @Size(min = 6, max = 254, message = "validation email size error")
    private String email;
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = "Field: mainPhoneNumber. Error: wrong phone format. Right examples:\n" +
                    "89261234567\n" +
                    "79261234567\n" +
                    "+7 926 123 45 67\n" +
                    "8(926)123-45-67\n" +
                    "123-45-67\n" +
                    "9261234567\n" +
                    "79261234567\n" +
                    "(495)1234567\n" +
                    "(495) 123 45 67\n" +
                    "89261234567\n" +
                    "8-926-123-45-67\n" +
                    "8 927 1234 234\n" +
                    "8 927 12 12 888\n" +
                    "8 927 12 555 12\n" +
                    "8 927 123 8 123")
    private String mainPhone;
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = "Field: mainPhone. Error: wrong phone format. Right examples:\n" +
                    "89261234567\n" +
                    "79261234567\n" +
                    "+7 926 123 45 67\n" +
                    "8(926)123-45-67\n" +
                    "123-45-67\n" +
                    "9261234567\n" +
                    "79261234567\n" +
                    "(495)1234567\n" +
                    "(495) 123 45 67\n" +
                    "89261234567\n" +
                    "8-926-123-45-67\n" +
                    "8 927 1234 234\n" +
                    "8 927 12 12 888\n" +
                    "8 927 12 555 12\n" +
                    "8 927 123 8 123")
    private String optionalPhone;
    private String instagram;
    private String youtube;
    private String facebook;
    private String tiktok;
    private String twitter;
    private String telegram;
    private String whatsapp;
    private String viber;
    private String vk;
    private MethodsOfCommunication preferCommunication;
    @Pattern(regexp = "^[АВЕКМНОРСТУХ]\\d{3}(?<!000)[АВЕКМНОРСТУХ]{2}\\d{2,3}$",
            message = "Field: carRegistrationNumber. Error: wrong carRegistrationNumber format.")
    private String carRegistrationNumber;
}
