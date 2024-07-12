package ru.practicum.events.privatear;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.privatear.service.PrivateEventService;
import ru.practicum.requests.dto.EventRequestStatusUpdate;
import ru.practicum.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.PathConstants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(PathConstants.USERS + PathConstants.USER_ID + PathConstants.EVENTS)
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    @GetMapping
    public List<EventShortDto> getAll(@PathVariable long userId,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Получение всех событий пользователя с id {}", userId);
        return privateEventService.getAll(userId, new PageParams(from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable long userId,
                             @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Сохранение нового события пользователем с id {}", userId);
        return privateEventService.save(userId, newEventDto);
    }

    @GetMapping(PathConstants.BY_ID)
    public EventFullDto getById(@PathVariable long userId,
                                @PathVariable long id) {
        log.info("Получение события c id {} пользователя с userId {}", id, userId);
        return privateEventService.getById(userId, id);
    }

    @PatchMapping(PathConstants.BY_ID)
    public EventFullDto update(@PathVariable long userId,
                               @PathVariable long id,
                               @Valid @RequestBody UpdateEventUserRequest updateEvent) {
        log.info("Обновление события с id {} пользвателем с userId {}", id, userId);
        return privateEventService.update(userId, id, updateEvent);
    }

    @GetMapping(PathConstants.BY_ID + PathConstants.REQUESTS)
    public List<ParticipationRequestDto> getRequests(@PathVariable long userId,
                                                     @PathVariable long id) {
        log.info("Получение списка всех заявок на событие с id {}", id);
        return privateEventService.getRequests(userId, id);
    }

    @PatchMapping(PathConstants.BY_ID + PathConstants.REQUESTS)
    public EventRequestStatusUpdateResult updateStatusRequests(@PathVariable long userId,
                                                               @PathVariable long id,
                                                               @RequestBody EventRequestStatusUpdate requestStatusUpdate) {
        log.info("Обновление статусов заявок на событие с id {}", id);
        return privateEventService.updateStatusRequests(userId, id, requestStatusUpdate);
    }

}
