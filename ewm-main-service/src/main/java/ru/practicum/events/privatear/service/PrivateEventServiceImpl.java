package ru.practicum.events.privatear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.DataIntegrityViolationException;
import ru.practicum.exceptions.IncorrectRequestException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.locations.dto.LocationMapper;
import ru.practicum.locations.repository.LocationRepository;
import ru.practicum.requests.dto.*;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.State;

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
        findUser(userId);
        return eventRepository
                .findAllByInitiatorId(userId, pageParams.getPageRequest())
                .getContent()
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto save(long userId, NewEventDto newEventDto) {
        checkDateTime(newEventDto.getEventDate());

        Event newEvent = eventMapper.toEvent(newEventDto);
        locationRepository.save(newEvent.getLocation());
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setInitiator(findUser(userId));
        newEvent.setState(State.PENDING);

        EventFullDto savedEvent = eventMapper.toEventFullDto(eventRepository.save(newEvent));
        //savedEvent.setViews();
        return savedEvent;
    }

    @Override
    public EventFullDto getById(long userId, long id) {
        findUser(userId);
        return eventMapper.toEventFullDto(findEvent(id));
    }

    @Override
    public EventFullDto update(long userId, long id, UpdateEventUserRequest updateEvent) {
        findUser(userId);
        Event event = findEvent(id);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new DataIntegrityViolationException("Only pending or canceled events can be changed");
        }

        LocalDateTime eventDate = updateEvent.getEventDate();
        if (eventDate != null) {
            checkDateTime(eventDate);
            event.setEventDate(eventDate);
        }

        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = findCategory(updateEvent.getCategory());
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(locationMapper.toLocation(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getStateAction() != null) {
            if (updateEvent.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            } else {
                event.setState(State.PENDING);
            }
        }

        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long userId, long id) {
        findUser(userId);
        findEvent(id);
        return requestRepository
                .findAllByRequesterIdAndEventId(userId, id)
                .stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateStatusRequests(long userId, long id, EventRequestStatusUpdate requestStatusUpdate) {
        findUser(userId);
        Event event = findEvent(id);
        List<Request> requests = requestRepository.findAllByRequesterIdAndEventIdAndIdIn(userId, id,
                requestStatusUpdate.getRequestIds());
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(new ArrayList<>(), new ArrayList<>());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            for (Request request : requests) {
                result.getConfirmedRequests().add(requestMapper.toRequestDto(request));
            }
        } else {
            int requestCountToLimit;
            int confirmedRequestCount = requestRepository.findCountByRequesterIdAndEventIdAndStatus(userId, id, StatusRequest.CONFIRMED);
            if (event.getParticipantLimit().equals(confirmedRequestCount)) {
                throw new DataIntegrityViolationException("The participant limit has been reached");
            } else {
                requestCountToLimit = event.getParticipantLimit() - confirmedRequestCount;
            }

            for (Request request : requests) {
                if (!request.getStatus().equals(StatusRequest.PENDING)) {
                    throw new DataIntegrityViolationException("Request must have status PENDING");
                }
                if (requestCountToLimit == 0 || requestStatusUpdate.getStatus().equals(StatusRequest.REJECTED)) {
                    request.setStatus(StatusRequest.REJECTED);
                    result.getRejectedRequests().add(requestMapper.toRequestDto(request));
                } else {
                    request.setStatus(StatusRequest.CONFIRMED);
                    result.getConfirmedRequests().add(requestMapper.toRequestDto(request));
                    requestCountToLimit--;
                }
            }
        }
        return result;
    }

    public User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    public Event findEvent(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", id)));
    }

    public Category findCategory(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", categoryId)));
    }

    public void checkDateTime(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IncorrectRequestException("Field: eventDate. " +
                    "Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + dateTime);
        }
    }
}
