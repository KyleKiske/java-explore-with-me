package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NewCategoryDto {
    @NotBlank(message = "Field: name. Error: field must not be blank.")
    private String name;
}
