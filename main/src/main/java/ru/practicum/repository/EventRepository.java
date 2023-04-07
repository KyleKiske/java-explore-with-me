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
            "E.eventDate BETWEEN :start AND :end " +
            "ORDER BY E.id")
    List<Event> findFilteredEventsAdmin(@Param("users") List<Long> users,
                                        @Param("states") List<String> states,
                                        @Param("categories") List<Long> categories,
                                        @Param("start") LocalDateTime rangeStart,
                                        @Param("end") LocalDateTime rangeEnd,
                                        Pageable pageable);


    @Query(value = "SELECT E FROM Event AS E " +
            "LEFT JOIN Category AS C on E.category.id = C.id " +
            "WHERE (upper(E.annotation) LIKE upper(concat('%', :text, '%')) OR " +
            "upper(E.description) like upper(concat('%', :text, '%')) ) AND " +
            "E.category.id in :categories AND " +
            "E.paid = :paid AND " +
            "E.eventDate BETWEEN :start AND :end AND " +
            "E.state = :state AND " +
            "(E.confirmedRequests < E.participantLimit OR " +
            "E.participantLimit <> 0) " +
            "ORDER BY E.id")
    List<Event> findPublicAvailable(@Param("text") String text,
                                    @Param("categories") List<Long> categories,
                                    @Param("paid") Boolean paid,
                                    @Param("start") LocalDateTime rangeStart,
                                    @Param("end") LocalDateTime rangeEnd,
                                    @Param("state") State state,
                                    Pageable pageable);

    List<Event> findByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategory_IdInAndPaidAndEventDateBetweenAndState(
            String text1,
            String text2,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            State state,
            Pageable pageable);

    List<Event> findAllByInitiatorId(long userId, Pageable pageable);

    Event findByInitiatorIdAndId(Long initiatorId, Long eventId);
}
