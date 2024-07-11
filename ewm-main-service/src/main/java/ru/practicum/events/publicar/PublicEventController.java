package ru.practicum.events.publicar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatsClient;
import ru.practicum.StatsRequest;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.GetEventsPublicRequest;
import ru.practicum.events.publicar.service.PublicEventService;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.PathConstants;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(PathConstants.EVENTS)
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final StatsClient statsClient;
    private final PublicEventService publicEventService;

    @Value("${server.application.name:ewm-service}")
    private String applicationName;

    @GetMapping
    public List<EventShortDto> getAll(@Valid GetEventsPublicRequest getEventsRequest,
                                      @Valid PageParams pageParams,
                                      HttpServletRequest request) {
        return publicEventService.getAll(getEventsRequest, pageParams, request);
    }

    @GetMapping(PathConstants.BY_ID)
    public EventFullDto getById(@PathVariable long id, HttpServletRequest request) {
        return publicEventService.getById(id, request);
    }
}
