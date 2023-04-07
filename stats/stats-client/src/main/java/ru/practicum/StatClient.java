package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ResponseStatsDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StatClient {

    private final RestTemplate rest;
    private final String serverUrl;

    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplate rest) {
        this.rest = rest;
        this.serverUrl = serverUrl;
    }

    public void createHit(EndpointHitDto endpointHitDto) {
        HttpEntity<EndpointHitDto> requestEntity = new HttpEntity<>(endpointHitDto);
        rest.exchange(serverUrl + "/hit", HttpMethod.POST, requestEntity, Object.class);
    }

    public List<ResponseStatsDto> get(String start, String end, List<String> uris, Boolean unique) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        if (!uris.isEmpty()) {
            parameters.put("uris", uris);
        }
        parameters.put("unique", unique);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<Object> response = rest
                .exchange(serverUrl + "/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                        HttpMethod.GET, requestEntity, Object.class, parameters);
        List<ResponseStatsDto> result = objectMapper.convertValue(response.getBody(), new TypeReference<>(){});
        return Objects.requireNonNullElseGet(result, List::of);
    }
}
