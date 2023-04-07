package ru.practicum.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
