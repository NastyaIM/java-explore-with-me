package ru.practicum.requests.privatear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.State;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.DataIntegrityException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.dto.StatusRequest;
import ru.practicum.requests.model.Request;
import ru.practicum.requests.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.GeneralMethods;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    @Override
    public List<ParticipationRequestDto> getAll(long userId) {
        return requestRepository
                .findAllByRequesterId(userId)
                .stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto save(long userId, long eventId) {
        User requester = GeneralMethods.findUser(userId, userRepository);
        Event event = GeneralMethods.findEvent(eventId, eventRepository);
        if (userId == event.getInitiator().getId()) {
            throw new DataIntegrityException("Initiator can't add a request to his event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new DataIntegrityException("Event must have status published");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit()
                .equals(event.getConfirmedRequests())) {
            throw new DataIntegrityException("The participant limit has been reached");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .build();
        updateStatusRequest(event, request);
        try {
            return requestMapper.toRequestDto(requestRepository.save(request));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public ParticipationRequestDto cancel(long userId, long id) {
        GeneralMethods.findUser(userId, userRepository);
        Request request = findRequest(id);
        request.setStatus(StatusRequest.CANCELED);
        return requestMapper.toRequestDto(request);
    }

    private void updateStatusRequest(Event event, Request request) {
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            request.setStatus(StatusRequest.CONFIRMED);
            eventRepository.save(event);
        } else {
            request.setStatus(StatusRequest.PENDING);
        }
    }

    private Request findRequest(long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", id)));
    }
}