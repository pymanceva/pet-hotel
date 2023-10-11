package ru.dogudacha.PetHotel.exception;

public class LimitException extends RuntimeException {
    public LimitException(String message) {
        super(message);
    }
}
