package ru.practicum.compilations.publicar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.CompilationRepository;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.utils.PageParams;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;

    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, PageParams pageParams) {
        return compilationRepository.findAll(pinned, pageParams.getPageRequest())
                .getContent()
                .stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(long id) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%s was not found", id)));
        return compilationMapper.toCompilationDto(compilation);
    }
}
