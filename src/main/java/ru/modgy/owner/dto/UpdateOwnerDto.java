package ru.modgy.owner.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOwnerDto {
    @Size(min = 2, max = 30, message = "Длина фамилии должна быть между {min} и {max}.")
    private String lastName;
    @Size(min = 2, max = 15, message = "Длина имени должна быть между {min} и {max}.")
    private String firstName;
    @Size(min = 2, max = 15, message = "Длина отчества должна быть между {min} и {max}.")
    private String middleName;
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = """
                    Неверный формат основного номера телефона. Правильные примеры:
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
    private String mainPhone;
    @Pattern(regexp = "^(8|\\+7)\\d{10}",
            message = """
                    Неверный формат дополнительного номера телефона. Правильные примеры:
                    89261234567
                    +79261234567
                    """)
    private String optionalPhone;
    @Size(max = 500, message = "Текст в поле 'прочие контакты' не может превышать {max} символов")
    private String otherContacts;
    @Size(max = 150, message = "Текст в поле 'фактический адрес' не может превышать {max} символов")
    private String actualAddress;
    @Size(max = 150, message = "Текст в поле 'доверенное лицо' не может превышать {max} символов")
    private String trustedMan;
    @Size(max = 150, message = "Текст в поле 'комментарий' не может превышать {max} символов")
    private String comment;
    @Size(max = 100, message = "Текст в поле 'откуда узнал о гостинице' не может превышать {max} символов")
    private String source;
    @Max(value = 10, message = "Значение 'рейтинг' должно быть не больше {value}")
    @PositiveOrZero(message = "В нашей компании не применяется отрицательный рейтинг")
    private Integer rating;
}
