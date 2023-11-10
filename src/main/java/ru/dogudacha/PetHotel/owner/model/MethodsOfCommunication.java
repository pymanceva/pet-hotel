package ru.dogudacha.PetHotel.owner.model;

import ru.dogudacha.PetHotel.user.model.Roles;

import java.util.Optional;

public enum MethodsOfCommunication {
    MAIN_PHONE,
    OPTIONAL_PHONE,
    TELEGRAM,
    WHATSAPP,
    VIBER,
    VK;

    public static Optional<MethodsOfCommunication> from(String stringState) {
        for (MethodsOfCommunication state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

    }
