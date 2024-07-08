package ru.practicum.events.privatear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.DataIntegrityViolationException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.locations.repository.LocationRepository;
import ru.practicum.requests.dto.EventRequestStatusUpdate;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    private final EventMapper eventMapper;

    @Override
    public List<EventShortDto> getAll(long userId, PageParams pageParams) {
        checkUserNotFound(userId);
        return eventRepository
                .findAllByInitiatorId(userId, pageParams.getPageRequest())
                .getContent()
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto save(long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new DataIntegrityViolationException("Field: eventDate. " +
                    "Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + newEventDto.getEventDate());
        }
        Event newEvent = eventMapper.toEvent(newEventDto);
        locationRepository.save(newEvent.getLocation());
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setInitiator(checkUserNotFound(userId));
        newEvent.setState(State.PENDING);
        EventFullDto savedEvent = eventMapper.toEventFullDto(eventRepository.save(newEvent));
        //savedEvent.setViews();
        return savedEvent;
    }

    @Override
    public EventFullDto getById(long userId, long id) {
        checkUserNotFound(userId);
        return eventMapper.toEventFullDto(eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", id))));
    }

    @Override
    public EventFullDto update(long userId, long id, UpdateEventUserRequest updateEvent) {
        return null;
    }

    @Override
    public ParticipationRequestDto getRequests(long userId, long id) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult updateStatusRequests(long userId, long id, EventRequestStatusUpdate requestStatusUpdate) {
        return null;
    }

    public User checkUserNotFound(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }
}
