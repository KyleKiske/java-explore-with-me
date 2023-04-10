package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.PaginationMaker;
import ru.practicum.StatClient;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.ResponseStatsDto;
import ru.practicum.repository.CompEventRepository;
import ru.practicum.repository.CompilationRepository;
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
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompEventRepository compEventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final StatClient statClient;
    static final String URI = "/events/";
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<CompilationDto> getFilteredCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(PaginationMaker.makePageRequest(from, size)).getContent();
        } else {
            compilations = compilationRepository.findByPinned(pinned, PaginationMaker.makePageRequest(from, size)).getContent();
        }
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Compilation compilation: compilations) {
            CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
            List<Long> eventIds = compEventRepository.findEventIdsByCompilationId(compilation.getId());
            List<Event> eventList = eventRepository.findAllById(eventIds);
            List<EventShortDto> eventShortDtoList = eventList.stream()
                    .map(eventMapper::eventToShortDto).collect(Collectors.toList());
            List<String> uris = new ArrayList<>();
            for (EventShortDto eventShortDto: eventShortDtoList) {
                uris.add(URI + eventShortDto.getId());
            }
            List<ResponseStatsDto> stats = statClient.get(LocalDateTime.now().minusYears(10).format(dateTimeFormatter),
                    LocalDateTime.now().plusYears(10).format(dateTimeFormatter), uris, false);
            Map<Long, Long> hits = new HashMap<>();

            if (stats != null && !stats.isEmpty() && stats.get(0).getUri().matches("\\d")) {
                for (ResponseStatsDto responseStatsDto: stats) {
                    Long id = Long.parseLong(responseStatsDto.getUri().split("/")[2]);
                    hits.put(id, responseStatsDto.getHits());
                }
                for (EventShortDto eventShortDto: eventShortDtoList) {
                    eventShortDto.setViews(hits.get(eventShortDto.getId()));
                }
            }
            compilationDto.setEvents(eventShortDtoList);
            compilationDtoList.add(compilationDto);
        }
        return compilationDtoList;

    }

    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId.toString()));

        List<Long> eventIds = compEventRepository.findEventIdsByCompilationId(compId);
        List<Event> eventList = eventRepository.findAllById(eventIds);
        List<EventShortDto> eventShortDtoList = eventList.stream()
                .map(eventMapper::eventToShortDto).collect(Collectors.toList());
        List<String> uris = new ArrayList<>();
        for (EventShortDto eventShortDto: eventShortDtoList) {
            uris.add(URI + eventShortDto.getId());
        }
        List<ResponseStatsDto> stats = statClient.get(LocalDateTime.now().minusYears(10).format(dateTimeFormatter),
                LocalDateTime.now().plusYears(10).format(dateTimeFormatter), uris, false);
        Map<Long, Long> hits = new HashMap<>();
        if (stats != null && !stats.isEmpty() && stats.get(0).getUri().matches("\\d")) {
            for (ResponseStatsDto responseStatsDto : stats) {
                Long id = Long.parseLong(responseStatsDto.getUri().split("/")[2]);
                hits.put(id, responseStatsDto.getHits());
            }
            for (EventShortDto eventShortDto : eventShortDtoList) {
                eventShortDto.setViews(hits.get(eventShortDto.getId()));
            }
        }
        CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
        compilationDto.setEvents(eventShortDtoList);
        return compilationDto;
    }
}
