package ru.practicum.dto.notDto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.dto.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

    public EventRequestStatusUpdateResult() {
        confirmedRequests = new ArrayList<>();
        rejectedRequests = new ArrayList<>();
    }
}
