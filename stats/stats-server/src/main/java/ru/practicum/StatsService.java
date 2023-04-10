package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ResponseStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public void createHit(EndpointHitDto endpointHitDto) {
        statsRepository.save(HitMapper.endpointHitDtoToEndpointHit(endpointHitDto));
    }

    public List<ResponseStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris != null) {
            for (int i = 0; i < uris.size(); i++) {
                if (uris.get(i).contains("[")) {
                    String replacement = uris.get(i);
                    replacement = replacement.replaceAll("\\[", "").replaceAll("]", "");
                    uris.set(i, replacement);
                }
            }
        }
        if (uris != null && !uris.isEmpty()) {
            if (unique) {
                return statsRepository.findAllHitsFromUniqueIPs(start, end, uris);
            }
            return statsRepository.findAllHits(start, end, uris);
        } else {
            if (unique) {
                return statsRepository.findAllHitsUrisEmpty(start, end);
            }
            return statsRepository.findAllHitsFromUniqueIPsUrisEmpty(start, end);
        }
    }
}
