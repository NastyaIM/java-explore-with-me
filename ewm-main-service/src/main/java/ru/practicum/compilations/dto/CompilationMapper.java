package ru.practicum.compilations.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.model.Event;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    @Mapping(target = "events", source = "newEvents")
    Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> newEvents);

    CompilationDto toCompilationDto(Compilation compilation);

}
