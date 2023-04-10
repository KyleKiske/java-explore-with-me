package ru.practicum.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    @NotBlank(message = "Field: text. Error: field must not be blank.")
    @Size(max = 1000, message = "Максимальная длина комментария - 1000 символов")
    private String text;
}
