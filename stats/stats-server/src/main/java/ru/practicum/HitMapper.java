package ru.practicum;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.model.EndpointHitDto;

@Component
@NoArgsConstructor
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
