package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank(message = "Field: title. Error: Field must not be blank.")
    private String title;
}
