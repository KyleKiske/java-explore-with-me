package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getFilteredEvents(
                    @RequestParam(required = false) String text,
                    @RequestParam(required = false) List<Long> categories,
                    @RequestParam(required = false) Boolean paid,
                    @RequestParam(required = false) String rangeStart,
                    @RequestParam(required = false) String rangeEnd,
                    @RequestParam(defaultValue = "false", required = false) Boolean onlyAvailable,
                    @RequestParam(required = false) String sort,
                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                    @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                    HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        List<EventShortDto> response = eventService.getFilteredEvents(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                ip);
        log.info("Получен список событий");
        return response;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable long id, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        EventFullDto event = eventService.getEventById(id, ip);
        log.info("Получено событие {}", id);
        return event;
    }

}
