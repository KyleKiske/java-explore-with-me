package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByEventId(Long eventId);

    List<Request> findByIdIn(List<Long> ids);

    List<Request> findAllByRequesterId(Long requesterId);

    Boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);
}
