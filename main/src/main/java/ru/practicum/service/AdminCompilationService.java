package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatClient;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.notDto.UpdateCompilationRequest;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.CompEvent;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.ResponseStatsDto;
import ru.practicum.repository.CompEventRepository;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCompilationService {
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompEventRepository compEventRepository;
    private final StatClient statClient;
    static final String URI = "/events/";
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.newCompilationToCompilation(newCompilationDto);
        List<Event> eventList = new ArrayList<>();
        if (newCompilationDto.getEvents() == null) {
            newCompilationDto.setEvents(List.of());
        } else {
            eventList = eventRepository.findAllById(newCompilationDto.getEvents());
            List<Long> eventResultList = eventList.stream().map(Event::getId).collect(Collectors.toList());
            List<Long> eventForRemove = eventList.stream().map(Event::getId).collect(Collectors.toList());
            List<Long> compilationEventIds = newCompilationDto.getEvents();
            compilationEventIds.removeAll(eventForRemove);
            if (compilationEventIds.size() != 0) {
                throw new EventNotFoundException(compilationEventIds.get(0).toString());
            }
            newCompilationDto.setEvents(eventResultList);
        }
        compilation = compilationRepository.save(compilation);
        for (Long eventId: newCompilationDto.getEvents()) {
            CompEvent compEvent = new CompEvent();
            compEvent.setEventId(eventId);
            compEvent.setCompilationId(compilation.getId());
            compEventRepository.save(compEvent);
        }

        CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
        if (newCompilationDto.getEvents() == null || newCompilationDto.getEvents().isEmpty()) {
            compilationDto.setEvents(List.of());
            return compilationDto;
        }
        List<EventShortDto> eventShortDtoList = eventList.stream()
                .map(eventMapper::eventToShortDto).collect(Collectors.toList());
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
        compilationDto.setEvents(eventShortDtoList);
        return compilationDto;
    }

    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new CompilationNotFoundException(compId.toString()));
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public CompilationDto redactCompilation(Long compId, UpdateCompilationRequest updateCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId.toString()));
        CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
        List<Event> eventList;

        if (updateCompilationDto.getEvents() != null)  {
            eventList = eventRepository.findAllById(updateCompilationDto.getEvents());
            List<Long> eventResultList = eventList.stream().map(Event::getId).collect(Collectors.toList());
            List<Long> eventForRemove = eventList.stream().map(Event::getId).collect(Collectors.toList());
            List<Long> compilationEventIds = updateCompilationDto.getEvents();
            compilationEventIds.removeAll(eventForRemove);
            if (compilationEventIds.size() != 0) {
                throw new EventNotFoundException(compilationEventIds.get(0).toString());
            }
            updateCompilationDto.setEvents(eventResultList);
            compilation = compilationRepository.save(compilation);

            compEventRepository.deleteByCompilationId(compilation.getId());
            for (Long eventId: updateCompilationDto.getEvents()) {
                CompEvent compEvent = new CompEvent();
                compEvent.setEventId(eventId);
                compEvent.setCompilationId(compilation.getId());
                compEventRepository.save(compEvent);
            }
            eventList = eventRepository.findAllById(updateCompilationDto.getEvents());
        } else {
            eventList = eventRepository.findAllById(compEventRepository.findEventIdsByCompilationId(compilation.getId()));
        }
        List<EventShortDto> eventShortDtoList = eventList.stream()
                .map(eventMapper::eventToShortDto).collect(Collectors.toList());
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
        compilationDto.setEvents(eventShortDtoList);

        if (updateCompilationDto.getPinned() != null) {
            compilationDto.setPinned(updateCompilationDto.getPinned());
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getTitle() != null && !updateCompilationDto.getTitle().isEmpty()) {
            compilationDto.setTitle(updateCompilationDto.getTitle());
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        compilationRepository.save(compilation);
        return compilationDto;
    }
}
