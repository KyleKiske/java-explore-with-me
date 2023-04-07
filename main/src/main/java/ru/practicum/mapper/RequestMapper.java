package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.Request;

@Component
public class RequestMapper {

    public ParticipationRequestDto requestToDto(Request request) {
        if (request == null) {
            return null;
        }
        ParticipationRequestDto requestDto = new ParticipationRequestDto();

        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setId(request.getId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }
}
