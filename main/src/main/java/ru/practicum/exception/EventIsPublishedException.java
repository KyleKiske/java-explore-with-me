package ru.practicum.exception;

public class EventIsPublishedException extends RuntimeException {
    public EventIsPublishedException(final String message) {
        super(message);
    }
}