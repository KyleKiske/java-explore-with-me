package ru.practicum.exception;

public class EventPublishedException extends RuntimeException {

    public EventPublishedException(String message) {
        super(message);
    }
}
