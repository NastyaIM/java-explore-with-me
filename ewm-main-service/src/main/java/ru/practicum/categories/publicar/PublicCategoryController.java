package ru.practicum.categories.publicar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.publicar.service.PublicCategoryService;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.PathConstants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(PathConstants.CATEGORIES)
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryController {
    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public List<CategoryDto> getAll(@Valid PageParams pageParams) {
        log.info("Получение списка всех категорий");
        return publicCategoryService.getAll(pageParams);
    }

    @GetMapping(PathConstants.BY_ID)
    public CategoryDto getById(@PathVariable long id) {
        log.info("Получение категории с id {}", id);
        return publicCategoryService.getById(id);
    }
}