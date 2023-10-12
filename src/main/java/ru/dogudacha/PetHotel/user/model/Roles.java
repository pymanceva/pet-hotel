package ru.dogudacha.PetHotel.user.model;

import java.util.Optional;

public enum Roles {
    BOSS,
    ADMIN,
    USER,
    FINANCIAL;

    public static Optional<Roles> from(String stringState) {
        for (Roles state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
