package ru.practicum.categories.adminar.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto save(NewCategoryDto newCategoryDto);

    void delete(long id);

    CategoryDto update(long id, NewCategoryDto newCategoryDto);
}
