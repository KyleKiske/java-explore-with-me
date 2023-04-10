package ru.practicum.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(final String message) {
        super(message);
    }
}