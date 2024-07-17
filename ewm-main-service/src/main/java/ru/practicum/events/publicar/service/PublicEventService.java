package ru.practicum.events.publicar.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.GetEventsPublicRequest;
import ru.practicum.locations.dto.GetEventByLocationRequest;
import ru.practicum.utils.PageParams;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getAll(GetEventsPublicRequest getPublicRequest, PageParams pageParams, HttpServletRequest request);

    EventFullDto getById(long id, HttpServletRequest request);

    List<EventShortDto> getByLocation(GetEventByLocationRequest getEventRequest, PageParams pageParams);
}
