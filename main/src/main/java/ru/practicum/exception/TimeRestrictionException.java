package ru.practicum.exception;

public class TimeRestrictionException extends RuntimeException {
    public TimeRestrictionException(String message) {
        super(message);
    }
}