package ru.practicum.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(final String message) {
        super(message);
    }
}