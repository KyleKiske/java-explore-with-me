package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.PaginationMaker;
import ru.practicum.StatClient;
import ru.practicum.dto.CompilationDto;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final StatClient statClient;
    static final String URI = "/events/";

    public List<CompilationDto> getFilteredCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(PaginationMaker.makePageRequest(from, size)).getContent();
            return compilations.stream()
                    .map(compilationMapper::compilationToCompilationDto)
                    .collect(Collectors.toList());
        }
        //СТАТА
        compilations = compilationRepository.findByPinned(pinned, PaginationMaker.makePageRequest(from, size));
        return compilations.stream()
                .map(compilationMapper::compilationToCompilationDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId.toString()));
        //СТАТА
        return compilationMapper.compilationToCompilationDto(compilation);
    }
}
