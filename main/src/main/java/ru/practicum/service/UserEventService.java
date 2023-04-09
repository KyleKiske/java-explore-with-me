package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PaginationMaker;
import ru.practicum.StatClient;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.notDto.EventRequestStatusUpdateRequest;
import ru.practicum.dto.notDto.EventRequestStatusUpdateResult;
import ru.practicum.dto.notDto.UpdateEventRequest;
import ru.practicum.exception.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.repository.*;

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
public class UserEventService {

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final StatClient statClient;
    static final String URI = "/events/";
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<EventShortDto> getEventsAddedByUser(long userId, Integer from, Integer size) {
        List<EventShortDto> dtoList = eventRepository
                .findAllByInitiatorId(userId, PaginationMaker.makePageRequest(from, size))
                .stream().map(eventMapper::eventToShortDto).collect(Collectors.toList());
        List<String> uris = new ArrayList<>();
        for (EventShortDto event: dtoList) {
            uris.add(URI + event.getId());
        }
        List<ResponseStatsDto> stats = statClient.get(LocalDateTime.now().minusYears(10).format(dateTimeFormatter),
                LocalDateTime.now().plusYears(10).format(dateTimeFormatter), uris, false);
        Map<Long, Long> hits = new HashMap<>();
        for (ResponseStatsDto responseStatsDto: stats) {
            Long id = Long.parseLong(responseStatsDto.getUri().split("/")[2]);
            hits.put(id, responseStatsDto.getHits());
        }
        for (EventShortDto eventShortDto: dtoList) {
            eventShortDto.setViews(hits.get(eventShortDto.getId()));
        }
        return dtoList;
    }

    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        Event event = eventMapper.newEventToEvent(newEventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new TimeRestrictionException("incorrect event date.");
        }
        if (newEventDto.getCategory() != null) {
            Category category = categoryRepository.findById(newEventDto.getCategory())
                    .orElseThrow(() -> new CategoryNotFoundException(newEventDto.getCategory().toString()));
            event.setCategory(category);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId.toString()));
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        eventRepository.save(event);
        return eventMapper.eventToFullDto(event);
    }

    public EventFullDto getEventById(long userId, Long eventId) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);
        if (event == null) {
            throw new EventNotFoundException(eventId.toString());
        }
        String fullUri = URI + eventId;
        String start = LocalDateTime.now().minusYears(10).format(dateTimeFormatter);
        String end = LocalDateTime.now().plusYears(10).format(dateTimeFormatter);
        List<ResponseStatsDto> stats = statClient.get(start,
                end, List.of(fullUri), false);
        EventFullDto result = eventMapper.eventToFullDto(event);
        if (stats.isEmpty()) {
            result.setViews(0L);
        } else {
            result.setViews(stats.get(0).getHits());
        }
        return result;
    }

    @Transactional
    public EventFullDto redactEventById(long userId, Long eventId, UpdateEventRequest updateEvent) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);
        if (event == null) {
            throw new EventNotFoundException(eventId.toString());
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new EventIsPublishedException("Only pending or canceled events can be redacted.");
        }
        if (updateEvent.getEventDate() != null && updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new TimeRestrictionException("incorrect event date.");
        }
        event = eventMapper.updateEventAdminToEvent(event, updateEvent);
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

    public List<ParticipationRequestDto> getRequestsForEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);
        if (event == null) {
            throw new EventNotFoundException(eventId.toString());
        }
        return requestRepository.findAllByEventId(eventId)
                .stream().map(requestMapper::requestToDto).collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResult redactRequestsStatus(long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId);
        if (event == null) {
            throw new EventNotFoundException(eventId.toString());
        }
        if (updateRequest.getRequestIds().isEmpty()) {
            return new EventRequestStatusUpdateResult();
        }
        List<Request> requests = requestRepository.findAllById(updateRequest.getRequestIds());
        for (Request req: requests) {
            req.setStatus(updateRequest.getStatus());
        }
        requestRepository.saveAll(requests);
        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();
        for (Request request: requests) {
            if (request.getStatus() == RequestStatus.CONFIRMED) {
                ParticipationRequestDto request1 = requestMapper.requestToDto(request);
                updateResult.getConfirmedRequests().add(request1);
            } else if (request.getStatus() == RequestStatus.REJECTED) {
                updateResult.getRejectedRequests().add(requestMapper.requestToDto(request));
            }
        }
        return updateResult;
    }
}
