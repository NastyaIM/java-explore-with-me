package ru.practicum.events.publicar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.GetStatsRequest;
import ru.practicum.StatsClient;
import ru.practicum.StatsRequest;
import ru.practicum.StatsResponse;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.exceptions.IncorrectRequestException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.utils.GeneralMethods;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.PathConstants;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    private final StatsClient statsClient;
    @Value("${server.application.name:ewm-service}")
    private String applicationName;

    private final EventMapper eventMapper;

    @Override
    public List<EventShortDto> getAll(GetEventsPublicRequest getPublicRequest,
                                      PageParams pageParams, HttpServletRequest request) {
        if (getPublicRequest.getRangeEnd() != null && getPublicRequest.getRangeStart() != null
                && getPublicRequest.getRangeEnd().isBefore(getPublicRequest.getRangeStart())) {
            throw new IncorrectRequestException("Incorrect dateTime");
        }
        if (getPublicRequest.getCategories() == null) {
            getPublicRequest.setCategories(categoryRepository.findAllId());
        }
        if (getPublicRequest.getRangeStart() == null && getPublicRequest.getRangeEnd() == null) {
            getPublicRequest.setRangeStart(LocalDateTime.now());
        }

        List<Event> events = eventRepository.search(getPublicRequest.getText(), getPublicRequest.getCategories(), getPublicRequest.getPaid(), getPublicRequest.getRangeStart(),
                        getPublicRequest.getRangeEnd(), getPublicRequest.isOnlyAvailable(), pageParams.getPageRequest())
                .getContent();

        Map<Long, Long> views = getEventsViews(events);
        sendHitRequestToStatsService(request);
        return sort(getPublicRequest.getSort(),
                events.stream()
                        .map(eventMapper::toEventShortDto)
                        .peek(event -> {
                            event.setViews(views.getOrDefault(event.getId(), 0L));
                        })
                        .collect(Collectors.toList()));
    }

    @Override
    public EventFullDto getById(long id, HttpServletRequest request) {
        Event event = GeneralMethods.findEvent(id, eventRepository);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(String.format("Event with id=%d was not found", id));
        }

        Map<Long, Long> views = getEventsViews(List.of(event));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        eventFullDto.setViews(views.getOrDefault(event.getId(), 0L));
        sendHitRequestToStatsService(request);
        return eventFullDto;
    }

    private void sendHitRequestToStatsService(HttpServletRequest request) {
        statsClient.hit(StatsRequest.builder()
                .app(applicationName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    public Map<Long, Long> getEventsViews(List<Event> events) {
        LocalDateTime start = events.stream()
                .map(Event::getCreatedOn)
                .min(LocalDateTime::compareTo)
                .orElseThrow(() -> new NotFoundException(""));
        List<String> uris = events.stream()
                .map(event -> String.format("/events/%s", event.getId()))
                .collect(Collectors.toList());
        List<StatsResponse> views = statsClient.stats(GetStatsRequest.builder()
                .start(start)
                .end(LocalDateTime.now())
                .uris(uris)
                .unique(true)
                .build());
        Map<Long, Long> eventViews = new HashMap<>();
        for (StatsResponse view : views) {
            if (view.getUri().equals(PathConstants.EVENTS)) {
                continue;
            }
            long eventId = Long.parseLong(view.getUri().substring(PathConstants.EVENTS.length() + 1));
            eventViews.put(eventId, view.getHits());
        }
        return eventViews;
    }

    private List<EventShortDto> sort(String sort, List<EventShortDto> events) {
        if (sort != null && sort.equals("EVENT_DATE")) {
            return events.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        } else if (sort != null && sort.equals("VIEWS")) {
            return events.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }
        return events;
    }
}