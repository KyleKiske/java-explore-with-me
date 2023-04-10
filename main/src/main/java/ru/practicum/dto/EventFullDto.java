package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.Category;
import ru.practicum.model.Location;
import ru.practicum.model.State;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventFullDto {
    private Long id;
    @Size(min = 20, message = "Минимальная длина краткого описания - 20 символов")
    @Size(max = 2000, message = "Максимальная длина краткого описания - 2000 символов")
    private String annotation;
    private Category category;
    private Integer confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @Size(min = 20, message = "Минимальная длина описания - 20 символов")
    @Size(max = 7000, message = "Максимальная длина описания - 7000 символов")
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    private String title;
    private Long views;
}
