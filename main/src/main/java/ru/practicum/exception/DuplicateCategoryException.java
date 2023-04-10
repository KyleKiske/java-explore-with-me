package ru.practicum.exception;

public class DuplicateCategoryException extends RuntimeException {
    public DuplicateCategoryException(final String message) {
        super(message);
    }
}