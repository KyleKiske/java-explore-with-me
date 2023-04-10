package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateCommentDto {
    @NotBlank(message = "Field: text. Error: field must not be blank.")
    @Size(max = 1000, message = "Максимальная длина комментария - 1000 символов")
    private String text;
}
