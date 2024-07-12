package ru.practicum.compilations.publicar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.publicar.service.PublicCompilationService;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.PathConstants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(PathConstants.COMPILATIONS)
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @Valid PageParams pageParams) {
        log.info("Получение списка компиляций с возможностью фильтрации");
        return publicCompilationService.getAll(pinned, pageParams);
    }

    @GetMapping(PathConstants.BY_ID)
    public CompilationDto getById(@PathVariable long id) {
        log.info("Получение компиляции c id {}", id);
        return publicCompilationService.getById(id);
    }
}
