package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserShortDto {

    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
