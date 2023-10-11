package ru.dogudacha.PetHotel.exception;

public class NotPublishedException extends RuntimeException {
    public NotPublishedException(String message) {
        super(message);
    }
}
