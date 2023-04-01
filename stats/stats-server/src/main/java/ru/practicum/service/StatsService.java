package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ResponseStatsDto;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    public void createHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = hitMapper.endpointHitDtoToEndpointHit(endpointHitDto);
        statsRepository.save(endpointHit);
    }

    public List<ResponseStatsDto> getStat(String start, String end, List<String> uris, boolean unique) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, dateTimeFormatter);

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statsRepository.findAllHitsFromUniqueIPsUrisEmpty(startDateTime, endDateTime);
            } else {
                return statsRepository.findAllHitsUrisEmpty(startDateTime, endDateTime);
            }
        } else {
            if (unique) {
                return statsRepository.findAllHitsFromUniqueIPs(startDateTime, endDateTime, uris);
            } else {
                return statsRepository.findAllHits(startDateTime, endDateTime, uris);
            }
        }


    }
}
