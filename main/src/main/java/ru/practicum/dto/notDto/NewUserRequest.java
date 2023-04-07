package ru.practicum.dto.notDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class NewUserRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
