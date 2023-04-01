package ru.practicum.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ResponseStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
