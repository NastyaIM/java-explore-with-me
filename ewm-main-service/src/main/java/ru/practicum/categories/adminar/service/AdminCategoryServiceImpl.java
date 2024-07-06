package ru.practicum.categories.adminar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.CategoryMapper;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(categoryMapper.toCategory(newCategoryDto));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void delete(long id) {
        checkNotFound(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto update(long id, NewCategoryDto newCategoryDto) {
        Category category = checkNotFound(id);
        category.setName(newCategoryDto.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    private Category checkNotFound(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", id)));
    }
}
