package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.PaginationMaker;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getFilteredCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PaginationMaker.makePageRequest(from, size)).getContent();
    }

    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId.toString()));
    }
}
