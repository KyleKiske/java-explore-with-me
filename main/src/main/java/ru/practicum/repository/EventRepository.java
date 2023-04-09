package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;
import ru.practicum.model.State;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT E FROM Event AS E " +
            "LEFT JOIN User AS U ON E.initiator.id = U.id " +
            "WHERE E.initiator.id IN :users AND " +
            "E.state IN :states AND " +
            "E.category.id IN :categories AND " +
            "E.eventDate BETWEEN :start AND :end AND " +
            "E.id BETWEEN :from AND :size " +
            "ORDER BY E.id")
    List<Event> findFilteredEventsAdmin(@Param("users") List<Long> users,
                                        @Param("states") List<State> states,
                                        @Param("categories") List<Long> categories,
                                        @Param("start") LocalDateTime rangeStart,
                                        @Param("end") LocalDateTime rangeEnd,
                                        @Param("from") Long from,
                                        @Param("size") Long size);


    @Query(value = "SELECT e from Event e " +
            "LEFT JOIN Category c ON e.category.id = c.id " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(concat('%', ?1, '%')) OR " +
            "UPPER(e.description) LIKE UPPER(concat('%', ?1, '%')) ) AND " +
            "e.category.id IN ?2 AND " +
            "e.paid = ?3 AND " +
            "e.eventDate BETWEEN ?4 AND ?5 AND " +
            "e.state = ?6 AND " +
            "e.id BETWEEN ?7 AND ?8 AND " +
            "(e.confirmedRequests < e.participantLimit OR " +
            "e.participantLimit <> 0) " +
            "GROUP BY e.id")
    List<Event> findPublicAvailable(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, State state, Long from, Long size);

    @Query(value = "SELECT e from Event e " +
            "LEFT JOIN Category c ON e.category.id = c.id " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(concat('%', ?1, '%')) OR " +
            "UPPER(e.description) LIKE UPPER(concat('%', ?1, '%')) ) AND " +
            "e.category.id IN ?2 AND " +
            "e.paid = ?3 AND " +
            "e.eventDate BETWEEN ?4 AND ?5 AND " +
            "e.state = ?6 AND " +
            "e.id BETWEEN ?7 AND ?8 " +
            "GROUP BY e.id")
    List<Event> findPublicNotAvailable(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, State state, Long from, Long size);

    List<Event> findAllByCategoryId(Long categoryId);

    List<Event> findAllByInitiatorId(long userId, Pageable pageable);

    Event findByInitiatorIdAndId(Long initiatorId, Long eventId);
}
