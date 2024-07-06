package ru.practicum.categories;

import org.mapstruct.Mapper;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(NewCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);
}
