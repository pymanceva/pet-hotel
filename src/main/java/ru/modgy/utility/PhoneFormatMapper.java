package ru.modgy.utility;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface PhoneFormatMapper {

    @Named("formatPhoneNumber")
    static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        String clearNumber = phoneNumber.replaceAll("\\D", "");
        int length = clearNumber.length();
        return "+7" + clearNumber.substring(length - 11, length);
    }
}
