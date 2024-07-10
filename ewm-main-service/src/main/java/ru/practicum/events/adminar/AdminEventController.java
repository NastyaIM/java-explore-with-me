package ru.practicum.events.adminar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.adminar.service.AdminEventService;
import ru.practicum.events.dto.*;
import ru.practicum.utils.Constants;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.PathConstants;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(PathConstants.ADMIN + PathConstants.EVENTS)
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(defaultValue = "") List<Long> users,
                                     @RequestParam(defaultValue = "") List<String> states,
                                     @RequestParam(defaultValue = "") List<Long> categories,
                                     @RequestParam(required = false)
                                     @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) LocalDateTime rangeStart,
                                     @RequestParam(required = false)
                                     @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) LocalDateTime rangeEnd,
                                     @RequestParam(defaultValue = "0") int from,
                                     @RequestParam(defaultValue = "10") int size) {
        List<State> statesSt = states.stream().map(State::from).collect(Collectors.toList());
        return adminEventService.getAll(new GetEventAdminRequest(users, statesSt, categories,
                rangeStart, rangeEnd, new PageParams(from, size)));
    }

    @PatchMapping(PathConstants.BY_ID)
    public EventFullDto update(@PathVariable long id,
                               @Valid @RequestBody UpdateEventAdminRequest request) {
        return adminEventService.update(id, request);
    }
}
