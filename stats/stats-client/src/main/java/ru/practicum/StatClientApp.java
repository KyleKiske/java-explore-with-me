package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ResponseStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class StatClientApp {

    private final RestTemplate rest;
    @Value("${stats-server.url}")
    private String serverUrl;
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void createHit(EndpointHitDto endpointHitDto) {
        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(endpointHitDto);
        rest.exchange(serverUrl + "hit", HttpMethod.POST, requestEntity, Object.class);
    }

    public List<ResponseStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(DATE_TIME_FORMATTER));
        parameters.put("end", end.format(DATE_TIME_FORMATTER));
        if (!uris.isEmpty()) {
            parameters.put("uris", uris);
        }
        parameters.put("unique", unique);
        HttpEntity<String> requestEntity = new HttpEntity<>("");
        ResponseEntity<ResponseStatsDto[]> response = rest
                .exchange(serverUrl + "stats", HttpMethod.GET, requestEntity, ResponseStatsDto[].class, parameters);
        ResponseStatsDto[] result = response.getBody();
        if (result == null) {
            return List.of();
        } else {
            return Arrays.asList(result);
        }
    }
}
