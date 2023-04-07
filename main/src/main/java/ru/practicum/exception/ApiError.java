package ru.practicum.exception;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}
