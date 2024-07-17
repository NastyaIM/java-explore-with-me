package ru.practicum.categories.dto;

import org.mapstruct.Mapper;
import ru.practicum.categories.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(NewCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);
}
