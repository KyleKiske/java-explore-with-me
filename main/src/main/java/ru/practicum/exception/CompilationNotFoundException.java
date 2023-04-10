package ru.practicum.exception;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(final String message) {
        super(message);
    }
}
