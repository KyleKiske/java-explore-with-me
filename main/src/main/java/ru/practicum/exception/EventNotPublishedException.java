package ru.practicum.exception;

public class EventNotPublishedException extends RuntimeException {
    public EventNotPublishedException(final String message) {
        super(message);
    }
}