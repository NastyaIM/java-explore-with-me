package ru.practicum.exceptions;

public class IncorrectRequestException extends RuntimeException {

    public IncorrectRequestException(String message) {
        super(message);
    }
}
