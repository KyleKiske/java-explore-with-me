package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Category;
import ru.practicum.model.Event;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
