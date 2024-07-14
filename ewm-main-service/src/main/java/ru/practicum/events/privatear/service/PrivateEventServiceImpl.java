package ru.practicum.events.privatear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.DataIntegrityException;
import ru.practicum.exceptions.IncorrectRequestException;
import ru.practicum.locations.dto.LocationMapper;
import ru.practicum.locations.repository.LocationRepository;
import ru.practicum.requests.dto.*;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.GeneralMethods;
import ru.practicum.utils.PageParams;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;

    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final RequestMapper requestMapper;

    @Override
    public List<EventShortDto> getAll(long userId, PageParams pageParams) {
        GeneralMethods.findUser(userId, userRepository);
        return eventRepository
                .findAllByInitiatorId(userId, pageParams.getPageRequest())
                .getContent()
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto save(long userId, NewEventDto newEventDto) {
        GeneralMethods.checkDateTime(newEventDto.getEventDate(), 2);

        Event newEvent = eventMapper.toEvent(newEventDto);
        locationRepository.save(newEvent.getLocation());
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setInitiator(GeneralMethods.findUser(userId, userRepository));
        newEvent.setState(State.PENDING);

        EventFullDto eventFullDto = eventMapper.toEventFullDto(eventRepository.save(newEvent));
        eventFullDto.setConfirmedRequests(0);

        return eventFullDto;
    }

    @Override
    public EventFullDto getById(long userId, long id) {
        GeneralMethods.findUser(userId, userRepository);
        return eventMapper.toEventFullDto(GeneralMethods.findEvent(id, eventRepository));
    }

    @Override
    public EventFullDto update(long userId, long id, UpdateEventUserRequest updateEvent) {
        GeneralMethods.findUser(userId, userRepository);
        Event event = GeneralMethods.findEvent(id, eventRepository);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new DataIntegrityException("Only pending or canceled events can be changed");
        }
        GeneralMethods.updateEventCommonFields(event, updateEvent, 2, categoryRepository, locationRepository, locationMapper);
        updateStatus(event, updateEvent);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId, long id) {
        GeneralMethods.findUser(userId, userRepository);
        Event event = GeneralMethods.findEvent(id, eventRepository);
        if (event.getInitiator().getId() != userId) {
            throw new IncorrectRequestException("");
        }
        return requestRepository
                .findAllByEventId(id)
                .stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateStatusRequests(long userId, long id, EventRequestStatusUpdate requestStatusUpdate) {
        GeneralMethods.findUser(userId, userRepository);
        Event event = GeneralMethods.findEvent(id, eventRepository);
        List<Request> requests = requestRepository.findAllByIdIn(requestStatusUpdate.getRequestIds());
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            for (Request request : requests) {
                confirmedRequests.add(requestMapper.toRequestDto(request));
            }
        } else {
            int requestCountToLimit;
            if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
                throw new DataIntegrityException("The participant limit has been reached");
            } else {
                requestCountToLimit = event.getParticipantLimit() - event.getConfirmedRequests();
            }

            for (Request request : requests) {
                if (!request.getStatus().equals(StatusRequest.PENDING)) {
                    throw new DataIntegrityException("Request must have status PENDING");
                }
                if (requestCountToLimit == 0 || requestStatusUpdate.getStatus().equals(StatusRequest.REJECTED)) {
                    request.setStatus(StatusRequest.REJECTED);
                    rejectedRequests.add(requestMapper.toRequestDto(requestRepository.save(request)));
                } else {
                    request.setStatus(StatusRequest.CONFIRMED);
                    confirmedRequests.add(requestMapper.toRequestDto(requestRepository.save(request)));
                    requestCountToLimit--;
                }
            }
        }
        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private void updateStatus(Event event, UpdateEventUserRequest updateEvent) {
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(UserStateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PENDING);
            }
        }
    }
}