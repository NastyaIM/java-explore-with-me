package ru.practicum.requests.privatear.service;

import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    List<ParticipationRequestDto> getAll(long userId);

    ParticipationRequestDto save(long userId, long eventId);

    ParticipationRequestDto cancel(long userId, long id);
}
