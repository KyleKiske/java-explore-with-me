package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;


    @Transactional
    public Category addCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.newCategoryToCategory(newCategoryDto);
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId.toString()));
        categoryRepository.deleteById(catId);
    }

    @Transactional
    public Category redactCategory(Long catId, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new CategoryNotFoundException(catId.toString()));
        category.setName(newCategoryDto.getName());
        return categoryRepository.save(category);
    }


}
