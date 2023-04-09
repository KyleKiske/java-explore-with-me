package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info("400 {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(final ConstraintViolationException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFound(final UserNotFoundException e) {
        log.error("404 user with id={} not found", e.getMessage(), e);
        return new ApiError(HttpStatus.NOT_FOUND.toString(),
                "Requested user not found.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(CompilationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCompilationNotFound(final CompilationNotFoundException e) {
        log.error("404 compilation with id={} not found", e.getMessage(), e);
        return new ApiError(HttpStatus.NOT_FOUND.toString(),
                "Requested compilation not found.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCategoryNotFound(final CategoryNotFoundException e) {
        log.error("404 category with id={} not found", e.getMessage(), e);
        return new ApiError(HttpStatus.NOT_FOUND.toString(),
                "Requested category not found.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventNotFound(final EventNotFoundException e) {
        log.error("404 event with id={} not found", e.getMessage(), e);
        return new ApiError(HttpStatus.NOT_FOUND.toString(),
                "Requested event not found.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(RequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleRequestNotFound(final RequestNotFoundException e) {
        log.error("404 request with id={} not found", e.getMessage(), e);
        return new ApiError(HttpStatus.NOT_FOUND.toString(),
                "Requested participation request not found.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicateEmail(final DuplicateEmailException e) {
        log.error("409 {}", e.getMessage(), e);
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "Email already taken.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDuplicateEmail(final DuplicateCategoryException e) {
        log.error("409 {}", e.getMessage(), e);
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "Category with that name already exists.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(TimeRestrictionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleTimeRestrictionException(final TimeRestrictionException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(RequestValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestValidationException(final RequestValidationException e) {
        log.error("409 {}", e.getMessage(), e);
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "Some of requirements in request not fulfilled.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(EventIsPublishedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventIsPublishedException(final EventIsPublishedException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "Some of requirements in request not fulfilled.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(EventPublishingException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventPublishingException(final EventPublishingException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }

    @ExceptionHandler(CategoryDeletionConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryDeletionConflictException(final CategoryDeletionConflictException e) {
        log.info("409 {}", e.getMessage());
        return new ApiError(HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter));
    }
}
