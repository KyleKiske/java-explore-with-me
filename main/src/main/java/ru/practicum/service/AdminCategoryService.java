package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.CategoryDeletionConflictException;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.DuplicateCategoryException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    private final CategoryMapper categoryMapper;

    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.newCategoryToCategory(newCategoryDto);
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new DuplicateCategoryException(newCategoryDto.getName());
        }
        categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDto(category);
    }

    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId.toString()));
        List<Event> eventList = eventRepository.findAllByCategoryId(catId);
        if (!eventList.isEmpty()) {
            throw new CategoryDeletionConflictException("The category has linked events.");
        }
        categoryRepository.deleteById(catId);
    }

    public CategoryDto redactCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId.toString()));
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new DuplicateCategoryException(newCategoryDto.getName());
        }
        category.setName(newCategoryDto.getName());
        categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDto(category);
    }


}
