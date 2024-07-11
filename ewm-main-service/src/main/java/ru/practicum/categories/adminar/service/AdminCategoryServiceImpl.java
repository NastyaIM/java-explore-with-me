package ru.practicum.categories.adminar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exceptions.DataIntegrityViolationException;
import ru.practicum.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        try {
            return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
        } catch (RuntimeException e) {
            throw new DataIntegrityViolationException("Category name is already exists");
        }
    }

    @Override
    public void delete(long id) {
        findCategory(id);
        try {
            categoryRepository.deleteById(id);
        } catch (RuntimeException e) {
            throw new DataIntegrityViolationException("The category is not empty");
        }
    }

    @Override
    public CategoryDto update(long id, NewCategoryDto newCategoryDto) {
        Category category = findCategory(id);
        try {
            category.setName(newCategoryDto.getName());
            return categoryMapper.toCategoryDto(categoryRepository.save(category));
        } catch (RuntimeException e) {
            throw new DataIntegrityViolationException("Category name is already exists");
        }
    }

    private Category findCategory(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", id)));
    }
}
