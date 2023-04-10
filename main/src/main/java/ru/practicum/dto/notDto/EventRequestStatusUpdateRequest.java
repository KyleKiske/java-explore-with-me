package ru.practicum.dto.notDto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.RequestStatus;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatus status;
}
