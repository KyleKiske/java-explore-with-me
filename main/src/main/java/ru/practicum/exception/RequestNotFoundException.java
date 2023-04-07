package ru.practicum.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(final String message) {
        super(message);
    }
}