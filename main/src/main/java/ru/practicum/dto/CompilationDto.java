package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CompilationDto {
    private List<EventShortDto> events;
    @NotNull
    private Long id;
    private Boolean pinned;
    @NotBlank
    private String title;
}
