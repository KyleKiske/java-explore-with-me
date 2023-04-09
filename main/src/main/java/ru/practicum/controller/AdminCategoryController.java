package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.model.Category;
import ru.practicum.service.AdminCategoryService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody @Validated NewCategoryDto newCategoryDto) {
        Category category = adminCategoryService.addCategory(newCategoryDto);
        log.info("Создана категория {}", category.getName());
        return category;
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryById(@PathVariable Long catId) {
        adminCategoryService.deleteCategory(catId);
        log.info("Категория {} удалена.", catId);
    }

    @PatchMapping("/{catId}")
    public Category redactCategory(@PathVariable long catId,
                                   @RequestBody @Valid NewCategoryDto newCategoryDto) {
        Category category = adminCategoryService.redactCategory(catId, newCategoryDto);
        log.info("Категория {} id={} изменена.", category.getName(), category.getId());
        return category;
    }
}
