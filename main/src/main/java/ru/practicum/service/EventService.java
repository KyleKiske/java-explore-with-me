package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.PaginationMaker;
import ru.practicum.StatClient;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.Event;
import ru.practicum.model.ResponseStatsDto;
import ru.practicum.model.State;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatClient statClient;
    static final String URI = "/events/";
    static final String APP = "main-event-service";
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<EventShortDto> getFilteredEvents(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 String rangeStart,
                                                 String rangeEnd,
                                                 Boolean onlyAvailable,
                                                 String sort,
                                                 Integer from,
                                                 Integer size,
                                                 String ip) {
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusYears(8);
        List<Event> eventList;

        if (text == null || text.isBlank()) {
            text = "";
        }
        if (categories == null || categories.isEmpty()) {
            categories = List.of();
        }
        if (rangeStart != null) {
            startDateTime = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        }
        if (rangeEnd != null) {
            endDateTime = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        }
        if (!onlyAvailable) {
            eventList = eventRepository.findByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategory_IdInAndPaidAndEventDateBetweenAndState(
                    text, text, categories, paid, startDateTime, endDateTime, State.PUBLISHED, PaginationMaker.makePageRequest(from, size));
        } else {
            eventList = eventRepository.findPublicAvailable(text, categories, paid, startDateTime, endDateTime, State.PUBLISHED,
                    PaginationMaker.makePageRequest(from, size));
        }
        List<EventShortDto> eventShortDtoList = eventList.stream().map(eventMapper::eventToShortDto)
                .collect(Collectors.toList());
        List<String> uris = new ArrayList<>();
        for (EventShortDto event: eventShortDtoList) {
            uris.add(URI + event.getId());
        }
        List<ResponseStatsDto> stats = statClient.get(LocalDateTime.now().minusYears(10).format(dateTimeFormatter),
                LocalDateTime.now().plusYears(10).format(dateTimeFormatter), uris, false);
        Map<Long, Long> hits = new HashMap<>();
        for (ResponseStatsDto responseStatsDto: stats) {
            Long id = Long.parseLong(responseStatsDto.getUri().split("/")[2]);
            hits.put(id, responseStatsDto.getHits());
        }
        for (EventShortDto eventShortDto: eventShortDtoList) {
            eventShortDto.setViews(hits.get(eventShortDto.getId()));
        }
        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                eventShortDtoList.sort(Comparator.comparing(EventShortDto::getEventDate));
            } else {
                eventShortDtoList.sort(Comparator.comparing(EventShortDto::getViews));
            }
        }
        EndpointHitDto endpointHitDto = new EndpointHitDto(APP, "/events", ip, LocalDateTime.now());
        statClient.createHit(endpointHitDto);
        return eventShortDtoList;
    }

    public EventFullDto getEventById(Long eventId, String ip) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EventNotFoundException(eventId.toString());
        }
        String fullUri = URI + eventId;
        List<ResponseStatsDto> stats = statClient.get(LocalDateTime.now().minusYears(10).format(dateTimeFormatter),
                LocalDateTime.now().plusYears(10).format(dateTimeFormatter), List.of(fullUri), false);
        EndpointHitDto endpointHitDto = new EndpointHitDto(APP, URI + event.getId(), ip, LocalDateTime.now());
        statClient.createHit(endpointHitDto);
        EventFullDto result = eventMapper.eventToFullDto(event);
        if (stats.isEmpty()) {
            result.setViews(0L);
        } else {
            result.setViews(stats.get(0).getHits());
        }
        return result;
    }
}
