package ru.practicum.compilations.adminar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.NotFoundException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Set<Event> events = findEventsByIds(newCompilationDto.getEvents());
        return compilationMapper
                .toCompilationDto(compilationRepository
                        .save(compilationMapper.toCompilation(newCompilationDto, events)));
    }

    @Override
    public void delete(long id) {
        findCompilation(id);
        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDto update(long id, UpdateCompilationDto updateCompilationDto) {
        Compilation compilation = findCompilation(id);
        if (updateCompilationDto.getEvents() != null) {
            Set<Event> events = findEventsByIds(updateCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getTitle() != null) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    private Compilation findCompilation(long id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%s was not found", id)));
    }

    private Set<Event> findEventsByIds(Set<Long> eventsIds) {
        return eventsIds.stream()
                .map(eventId -> eventRepository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException(String.format("Event with id=%s was not found", eventId))))
                .collect(Collectors.toSet());
    }
}