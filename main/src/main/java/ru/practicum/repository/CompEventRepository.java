package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.CompEvent;

import java.util.List;

public interface CompEventRepository extends JpaRepository<CompEvent, Long> {

    void deleteByCompilationId(Long compilationId);

    @Query(value = "SELECT C.eventId from CompEvent AS C " +
            "where C.compilationId = :compilationId " +
            "group by C.eventId")
    List<Long> findEventIdsByCompilationId(@Param("compilationId") Long compilationId);
}
