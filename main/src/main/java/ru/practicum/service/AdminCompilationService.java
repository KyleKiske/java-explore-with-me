package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.notDto.UpdateCompilationRequest;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCompilationService {
    
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.newCompilationToCompilation(newCompilationDto);
        compilation = compilationRepository.save(compilation);
        CompilationDto compilationDto = compilationMapper.compilationToCompilationDto(compilation);
        List<Event> eventList = eventRepository.findAllById(newCompilationDto.getEvents());
        List<EventShortDto> eventShortDtoList = eventList.stream()
                .map(eventMapper::eventToShortDto).collect(Collectors.toList());
        //СТАТА
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
        if (updateCompilationDto.getPinned() != null) {
            compilationDto.setPinned(updateCompilationDto.getPinned());
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getEvents() != null) {
            List<Event> eventList = eventRepository.findAllById(updateCompilationDto.getEvents());
            List<EventShortDto> eventShortDtoList = eventList.stream()
                    .map(eventMapper::eventToShortDto).collect(Collectors.toList());
            compilationDto.setEvents(eventShortDtoList);
            compilation.setEventsInCompilations(eventList);
        }
        if (updateCompilationDto.getTitle() != null) {
            compilationDto.setTitle(updateCompilationDto.getTitle());
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        //СТАТА
        compilationRepository.save(compilation);
        return compilationDto;
    }
}
