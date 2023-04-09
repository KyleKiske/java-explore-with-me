package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findByPinnedAndIdBetween(Boolean pinned, Long from, Long size);

    List<Compilation> findAllByIdBetween(Long from, Long size);

}
