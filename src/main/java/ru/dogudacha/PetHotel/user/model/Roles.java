package ru.dogudacha.PetHotel.user.model;

import java.util.Optional;

public enum Roles {
    ROLE_BOSS,
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_FINANCIAL;

    public static Optional<Roles> from(String stringState) {
        for (Roles state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
