package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.Category;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventShortDto {
    @Size(min = 20, message = "Минимальная длина краткого описания - 20 символов")
    @Size(max = 2000, message = "Максимальная длина краткого описания - 2000 символов")
    private String annotation;
    @NotNull
    private Category category;
    private Integer confirmedRequests;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
