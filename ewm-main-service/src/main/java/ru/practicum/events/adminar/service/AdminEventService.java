package ru.practicum.events.adminar.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.GetEventAdminRequest;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.utils.PageParams;

import java.util.List;

public interface AdminEventService {
    List<EventFullDto> getAll(GetEventAdminRequest request, PageParams pageParams);

    EventFullDto update(long id, UpdateEventAdminRequest request);
}
