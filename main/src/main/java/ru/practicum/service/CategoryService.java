package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getFilteredCategories(Long from, Long size) {
        size = size + from - 1;
        return categoryRepository.findAllByIdBetween(from, size);
    }

    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId.toString()));
    }
}
