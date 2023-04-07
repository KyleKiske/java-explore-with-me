package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getFilteredCompilations(
                @RequestParam(required = false) Boolean pinned,
                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<CompilationDto> response = compilationService.getFilteredCompilations(pinned, from, size);
        log.info("Получен список подборок");
        return response;
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable long compId) {
        CompilationDto compilation = compilationService.getCompilationById(compId);
        log.info("Получена подборка {}", compId);
        return compilation;
    }
}
