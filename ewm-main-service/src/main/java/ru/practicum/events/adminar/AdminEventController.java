package ru.practicum.events.adminar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.adminar.service.AdminEventService;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.GetEventAdminRequest;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.PathConstants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(PathConstants.ADMIN + PathConstants.EVENTS)
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> getAll(@Valid GetEventAdminRequest request,
                                     @Valid PageParams pageParams) {
        log.info("Получение списка событий с возможной фильтрацией");
        return adminEventService.getAll(request, pageParams);
    }

    @PatchMapping(PathConstants.BY_ID)
    public EventFullDto update(@PathVariable long id,
                               @Valid @RequestBody UpdateEventAdminRequest request) {
        log.info("Обновление события с id {}", id);
        return adminEventService.update(id, request);
    }
}
