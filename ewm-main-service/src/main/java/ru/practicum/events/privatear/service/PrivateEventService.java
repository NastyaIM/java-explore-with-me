package ru.practicum.events.privatear.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.requests.dto.EventRequestStatusUpdate;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.utils.PageParams;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getAll(long userId, PageParams pageParams);

    EventFullDto save(long userId, NewEventDto newEventDto);

    EventFullDto getById(long userId, long id);

    EventFullDto update(long userId, long id, UpdateEventUserRequest updateEvent);

    List<ParticipationRequestDto> getRequests(long userId, long id);

    EventRequestStatusUpdateResult updateStatusRequests(long userId, long id,
                                                        EventRequestStatusUpdate requestStatusUpdate);
}
