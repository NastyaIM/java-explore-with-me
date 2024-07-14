package ru.practicum.events.adminar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.DataIntegrityException;
import ru.practicum.locations.dto.LocationMapper;
import ru.practicum.locations.repository.LocationRepository;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.GeneralMethods;
import ru.practicum.utils.PageParams;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;


    @Override
    public List<EventFullDto> getAll(GetEventAdminRequest request, PageParams pageParams) {
        updateNullFields(request);

        Pageable pageable = pageParams.getPageRequest();
        List<Event> events;
        LocalDateTime start = request.getRangeStart();
        LocalDateTime end = request.getRangeEnd();

        if (start != null && end != null) {
            events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(request.getUsers(),
                    request.getStates(), request.getCategories(),
                    start, end, pageable).getContent();
        } else if (start != null) {
            events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfter(request.getUsers(),
                    request.getStates(), request.getCategories(),
                    start, pageable).getContent();
        } else if (end != null) {
            events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsBefore(request.getUsers(),
                    request.getStates(), request.getCategories(),
                    end, pageable).getContent();
        } else {
            events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdIn(request.getUsers(),
                    request.getStates(), request.getCategories(), pageable).getContent();
        }

        return events.stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto update(long id, UpdateEventAdminRequest request) {
        Event event = GeneralMethods.findEvent(id, eventRepository);
        GeneralMethods.updateEventCommonFields(event, request, 1, categoryRepository, locationRepository, locationMapper);
        updateState(request.getStateAction(), event);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    private void updateNullFields(GetEventAdminRequest request) {
        if (request.getUsers() == null) {
            request.setUsers(userRepository.findAllId());
        }
        if (request.getCategories() == null) {
            request.setCategories(categoryRepository.findAllId());
        }
        if (request.getStates() == null) {
            request.setStates(List.of(State.PUBLISHED, State.CANCELED, State.PENDING));
        }
    }

    private void updateState(AdminStateAction stateAction, Event event) {
        if (stateAction != null) {
            if (stateAction.equals(AdminStateAction.PUBLISH_EVENT)) {
                if (event.getState().equals(State.PENDING)) {
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new DataIntegrityException("Event must have state PENDING");
                }
            } else {
                if (event.getState().equals(State.PUBLISHED)) {
                    throw new DataIntegrityException("Event must have state PENDING or CANCELED");
                } else {
                    event.setState(State.CANCELED);
                }
            }
        }
    }
}