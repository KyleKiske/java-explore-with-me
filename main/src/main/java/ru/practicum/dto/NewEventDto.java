package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class NewEventDto {
    @Size(min = 20, message = "Минимальная длина краткого описания - 20 символов")
    @Size(max = 2000, message = "Максимальная длина краткого описания - 2000 символов")
    private String annotation;
    @NotNull
    private Long category;
    @Size(min = 20, message = "Минимальная длина описания - 20 символов")
    @Size(max = 7000, message = "Максимальная длина описания - 7000 символов")
    @NotBlank(message = "Field: description. Error: Field must not be blank.")
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;

    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, message = "Минимальная длина названия - 3 символа")
    @Size(max = 120, message = "Максимальная длина названия - 120 символов")
    private String title;
}
