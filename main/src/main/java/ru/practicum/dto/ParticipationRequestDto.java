package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipationRequestDto {

    private Long id;
    private Long event;
    private Long requester;
    private LocalDateTime created;
    private RequestStatus status;
}
