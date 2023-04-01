package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ResponseStatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void postHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        statsService.createHit(endpointHitDto);
        log.info("Информания о запросе добавлена.");
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseStatsDto> getStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        List<ResponseStatsDto> response = statsService.getStat(start, end, uris, unique);
        log.info("Статистика сформирована.");
        return response;
    }
}
