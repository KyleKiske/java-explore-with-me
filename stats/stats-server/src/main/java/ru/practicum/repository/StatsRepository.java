package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ResponseStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.model.ResponseStatsDto(E.uri, E.app, count(E.ip)) FROM EndpointHit as E " +
            "WHERE (E.timestamp BETWEEN :start AND :end) " +
            "AND (E.uri IN :uris) " +
            "GROUP BY E.app, E.uri " +
            "ORDER BY count(E.ip) DESC")
    List<ResponseStatsDto> findAllHits(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.model.ResponseStatsDto(E.uri, E.app, count(DISTINCT E.ip)) FROM EndpointHit as E " +
            "WHERE (E.timestamp BETWEEN :start AND :end) " +
            "AND (E.uri IN :uris) " +
            "GROUP BY E.app, E.uri " +
            "ORDER BY count(E.ip) DESC")
    List<ResponseStatsDto> findAllHitsFromUniqueIPs(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.model.ResponseStatsDto(E.uri, E.app, count(E.ip)) FROM EndpointHit as E " +
            "WHERE (E.timestamp BETWEEN :start AND :end) " +
            "GROUP BY E.app, E.uri " +
            "ORDER BY count(E.ip) DESC")
    List<ResponseStatsDto> findAllHitsUrisEmpty(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ResponseStatsDto(E.uri, E.app, count(DISTINCT E.ip)) FROM EndpointHit as E " +
            "WHERE (E.timestamp BETWEEN :start AND :end) " +
            "GROUP BY E.app, E.uri " +
            "ORDER BY count(E.ip) DESC")
    List<ResponseStatsDto> findAllHitsFromUniqueIPsUrisEmpty(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);


}
