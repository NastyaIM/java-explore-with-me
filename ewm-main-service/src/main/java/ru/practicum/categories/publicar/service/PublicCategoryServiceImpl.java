package ru.practicum.categories.publicar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.utils.PageParams;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getAll(PageParams pageParams) {
        return categoryRepository
                .findAll(pageParams.getPageRequest())
                .getContent()
                .stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", id)));
        return categoryMapper.toCategoryDto(category);
    }
}