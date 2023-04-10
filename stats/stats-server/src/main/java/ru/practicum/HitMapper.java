package ru.practicum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.model.EndpointHitDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitMapper {

    public static EndpointHit endpointHitDtoToEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setTimestamp(endpointHitDto.getTimestamp());
        return endpointHit;
    }
}
