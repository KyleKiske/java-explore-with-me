package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private LocalDateTime created;
    private Long eventId;
    private User commentator;
    private String text;
}
