package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.PaginationMaker;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDto> getFilteredCategories(Integer from, Integer size) {
        List<Category> categoryList = categoryRepository.findAll(PaginationMaker.makePageRequest(from, size)).getContent();
        return categoryList.stream().map(categoryMapper::categoryToCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException(catId.toString()));
        return categoryMapper.categoryToCategoryDto(category);
    }
}
