package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.model.Category;

@Component
public class CategoryMapper {

    public Category newCategoryToCategory(NewCategoryDto newCategoryDto) {
        if (newCategoryDto == null) {
            return null;
        }
        Category category = new Category();

        category.setName(newCategoryDto.getName());

        return category;
    }
}

