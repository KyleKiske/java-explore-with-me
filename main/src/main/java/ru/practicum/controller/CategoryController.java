package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.Category;
import ru.practicum.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getFilteredCategories(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Long from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Long size) {
        List<Category> response = categoryService.getFilteredCategories(from, size);
        log.info("Получен список категорий");
        return response;
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public Category getCategoryById(@PathVariable Long catId) {
        Category category = categoryService.getCategoryById(catId);
        log.info("Получена категория {}", catId);
        return category;
    }
}