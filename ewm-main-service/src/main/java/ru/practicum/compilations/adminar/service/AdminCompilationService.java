package ru.practicum.compilations.adminar.service;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationDto;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(long id);

    CompilationDto update(long id, UpdateCompilationDto updateCompilationDto);
}
