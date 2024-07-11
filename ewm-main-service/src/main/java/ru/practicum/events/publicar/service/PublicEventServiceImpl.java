package ru.practicum.events.publicar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.IncorrectRequestException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.utils.PageParams;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    @Override
    public List<EventShortDto> getAll(GetEventsPublicRequest request, PageParams pageParams) {
        if (request.getRangeEnd() != null && request.getRangeStart() != null
                && request.getRangeEnd().isBefore(request.getRangeStart())) {
            throw new IncorrectRequestException("");
        }
        if (request.getCategories() == null) {
            request.setCategories(categoryRepository.findAllId());
        }
        if (request.getRangeStart() == null && request.getRangeEnd() == null) {
            request.setRangeStart(LocalDateTime.now());
        }

        List<EventShortDto> events = eventRepository
                .search(request.getText(), request.getCategories(), request.getPaid(), request.getRangeStart(),
                        request.getRangeEnd(), request.isOnlyAvailable(), pageParams.getPageRequest())
                .getContent()
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        if (request.getSort() != null && request.getSort().equals("EVENT_DATE")) {
            return events.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        } else if (request.getSort() != null && request.getSort().equals("VIEWS")) {
            return events.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }
        return events;
    }

    @Override
    public EventFullDto getById(long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", id)));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(String.format("Event with id=%d was not found", id));
        }
        return eventMapper.toEventFullDto(event);
    }
}
