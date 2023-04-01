package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.EndpointHitDto;

@Component
public class HitMapper {

    public EndpointHit endpointHitDtoToEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setTimestamp(endpointHitDto.getTimestamp());
        return endpointHit;
    }
}
