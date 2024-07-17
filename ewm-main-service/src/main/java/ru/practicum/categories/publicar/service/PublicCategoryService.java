package ru.practicum.categories.publicar.service;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.utils.PageParams;

import java.util.List;

public interface PublicCategoryService {
    List<CategoryDto> getAll(PageParams pageParams);

    CategoryDto getById(long id);
}
