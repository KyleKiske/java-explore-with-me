package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.notDto.UpdateCompilationRequest;
import ru.practicum.service.AdminCompilationService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        CompilationDto compilationDto = adminCompilationService.addCompilation(newCompilationDto);
        log.info("Создана подборка {}", compilationDto.getTitle());
        return compilationDto;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable long compId) {
        adminCompilationService.deleteCompilation(compId);
        log.info("Подборка {} удалена.", compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto redactCompilationById(@PathVariable long compId,
                                                @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        CompilationDto compilationDto = adminCompilationService.redactCompilation(compId, updateCompilationRequest);
        log.info("Подборка {} изменена.", compilationDto.getId());
        return compilationDto;
    }
}
