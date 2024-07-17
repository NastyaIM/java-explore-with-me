package ru.practicum.compilations.publicar.service;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.utils.PageParams;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getAll(Boolean pinned, PageParams pageParams);

    CompilationDto getById(long id);
}
