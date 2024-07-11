package ru.practicum.events.publicar.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.GetEventsPublicRequest;
import ru.practicum.utils.PageParams;

import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getAll(GetEventsPublicRequest request, PageParams pageParams);

    EventFullDto getById(long id);
}
