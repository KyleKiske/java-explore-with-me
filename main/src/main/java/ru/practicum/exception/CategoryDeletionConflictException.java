package ru.practicum.exception;

public class CategoryDeletionConflictException extends RuntimeException {
    public CategoryDeletionConflictException(final String message) {
        super(message);
    }
}