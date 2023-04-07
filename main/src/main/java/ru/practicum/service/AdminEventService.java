package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PaginationMaker;
import ru.practicum.StatClient;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.notDto.StateAction;
import ru.practicum.dto.notDto.UpdateEventRequest;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.EventPublishingException;
import ru.practicum.exception.TimeRestrictionException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.ResponseStatsDto;
import ru.practicum.model.State;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final StatClient statClient;
    static final String URI = "/events/";
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<EventFullDto> getFilteredEvents(List<Long> users,
                                                List<String> states,
                                                List<Long> categories,
                                                String rangeStart,
                                                String rangeEnd,
                                                Integer from,
                                                Integer size) {
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusYears(8);
        if (users == null) {
            users = new ArrayList<>();
        }
        if (states == null) {
            states = new ArrayList<>();
        }
        if (categories == null) {
            categories = new ArrayList<>();
        }
        if (rangeStart != null) {
            startDateTime = LocalDateTime.parse(rangeStart, dateTimeFormatter);
        }
        if (rangeEnd != null) {
            endDateTime = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        }
        List<Event> eventList = eventRepository.findFilteredEventsAdmin(users, states, categories, startDateTime, endDateTime,
                PaginationMaker.makePageRequest(from, size));
        List<EventFullDto> fullDtoList =  eventList.stream().map(eventMapper::eventToFullDto)
                .collect(Collectors.toList());
        List<String> uris = new ArrayList<>();
        for (EventFullDto event: fullDtoList) {
            uris.add(URI + event.getId());
        }
        List<ResponseStatsDto> stats = statClient.get(LocalDateTime.now().minusYears(10).format(dateTimeFormatter),
                LocalDateTime.now().plusYears(10).format(dateTimeFormatter), uris, false);
        Map<Long, Long> hits = new HashMap<>();
        for (ResponseStatsDto responseStatsDto: stats) {
            Long id = Long.parseLong(responseStatsDto.getUri().split("/")[2]);
            hits.put(id, responseStatsDto.getHits());
        }
        for (EventFullDto eventFullDto: fullDtoList) {
            eventFullDto.setViews(hits.get(eventFullDto.getId()));
        }
        return fullDtoList;
    }

    @Transactional
    public EventFullDto redactEventInfo(Long eventId, UpdateEventRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId.toString()));
        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new TimeRestrictionException("Field: eventDate. Error: incorrect event date.");
            }
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(updateEvent.getCategory().toString()));
            event.setCategory(category);
        }
        event = eventMapper.updateEventAdminToEvent(event, updateEvent);
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                if (!event.getState().equals(State.PENDING)) {
                    throw new EventPublishingException("Can't publish published or cancelled event.");
                }
                event.setState(State.PUBLISHED);
            } else {
                if (event.getState().equals(State.PUBLISHED)) {
                    throw new EventPublishingException("Can't cancel published event.");
                }
                event.setState(State.CANCELED);
            }
        }
        eventRepository.save(event);
        String fullUri = URI + eventId;
        List<ResponseStatsDto> stats = statClient.get(LocalDateTime.now().minusYears(10).format(dateTimeFormatter),
                LocalDateTime.now().plusYears(10).format(dateTimeFormatter), List.of(fullUri), false);
        EventFullDto result = eventMapper.eventToFullDto(event);
        if (stats.isEmpty()) {
            result.setViews(0L);
        } else {
            result.setViews(stats.get(0).getHits());
        }
        return result;
    }
}
