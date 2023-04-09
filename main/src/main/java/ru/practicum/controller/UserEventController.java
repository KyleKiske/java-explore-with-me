package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.notDto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.notDto.EventRequestStatusUpdateResult;
import ru.practicum.dto.notDto.UpdateEventRequest;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.UserEventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "users/{userId}/events")
@RequiredArgsConstructor
public class UserEventController {

    private final UserEventService userEventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEventsAddedByUser(
            @PathVariable long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<EventShortDto> response = userEventService.getEventsAddedByUser(
                userId,
                from,
                size);
        log.info("Получен список событий пользователя {}", userId);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(@PathVariable long userId,
                                    @RequestBody NewEventDto event) {
        EventFullDto eventAdded = userEventService.addEvent(userId, event);
        log.info("Создано новое событие");
        return eventAdded;
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable long userId,
                                     @PathVariable long eventId) {
        EventFullDto event = userEventService.getEventById(userId, eventId);
        log.info("Получено событие {}", eventId);
        return event;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto redactEventById(@PathVariable long userId,
                                 @PathVariable long eventId,
                                 @RequestBody UpdateEventRequest event) {
        EventFullDto eventRedacted = userEventService.redactEventById(userId, eventId, event);
        log.info("Событие {} изменено", eventId);
        return eventRedacted;
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsForEventById(@PathVariable long userId,
                                                 @PathVariable long eventId) {
        List<ParticipationRequestDto> requestList = userEventService.getRequestsForEvent(userId, eventId);
        log.info("Получен список заявок на участие в событии {}", eventId);
        return requestList;
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult redactRequestsForEventById(
                                    @PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        EventRequestStatusUpdateResult updateResult = userEventService.redactRequestsStatus(userId, eventId, updateRequest);
        log.info("Изменен статус заявок на участие в событии {}", eventId);
        return updateResult;
    }
}
