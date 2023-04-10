package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.notDto.UpdateEventRequest;
import ru.practicum.model.State;
import ru.practicum.service.AdminEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getFilteredEvents(
                @RequestParam(required = false) List<Long> users,
                @RequestParam(required = false) List<State> states,
                @RequestParam(required = false) List<Long> categories,
                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<EventFullDto> response = adminEventService.getFilteredEvents(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size);
        log.info("Получен список событий, сформированный по запросу администратора.");
        return response;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto redactEventById(@PathVariable long eventId,
                                 @RequestBody @Valid UpdateEventRequest updateEvent) {
        EventFullDto eventFullDto = adminEventService.redactEventInfo(eventId, updateEvent);
        log.info("Событие {} было изменено администратором", eventId);
        return eventFullDto;
    }
}
