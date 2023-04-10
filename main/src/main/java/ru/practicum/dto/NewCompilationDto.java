package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {
    @NotBlank(message = "Field: title. Error: Field must not be blank.")
    private String title;
    private Boolean pinned;
    private List<Long> events;
}
