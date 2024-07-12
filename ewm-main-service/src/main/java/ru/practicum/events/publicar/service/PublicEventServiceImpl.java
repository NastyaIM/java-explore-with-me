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
import ru.practicum.utils.PageParams;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    private final StatsClient statsClient;
    @Value("${server.application.name:ewm-service}")
    private final String applicationName;

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

        sendHitRequestToStatsService(request);
        List<Event> events = eventRepository.search(getPublicRequest.getText(), getPublicRequest.getCategories(), getPublicRequest.getPaid(), getPublicRequest.getRangeStart(),
                        getPublicRequest.getRangeEnd(), getPublicRequest.isOnlyAvailable(), pageParams.getPageRequest())
                .getContent();

        events.forEach(this::setEventViews);

        return sort(getPublicRequest.getSort(),
                events.stream()
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public EventFullDto getById(long id, HttpServletRequest request) {
        sendHitRequestToStatsService(request);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", id)));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(String.format("Event with id=%d was not found", id));
        }
        setEventViews(event);
        return eventMapper.toEventFullDto(event);
    }

    private void sendHitRequestToStatsService(HttpServletRequest request) {
        statsClient.hit(StatsRequest.builder()
                .app(applicationName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build());
    }

    public void setEventViews(Event event) {
        List<StatsResponse> views = statsClient.stats(GetStatsRequest.builder()
                .start(event.getPublishedOn())
                .end(LocalDateTime.now())
                .uris(List.of("/events/" + event.getId()))
                .unique(true)
                .build());
        if (views.isEmpty()) {
            event.setViews(0L);
        } else {
            event.setViews(views.get(0).getHits());
        }
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