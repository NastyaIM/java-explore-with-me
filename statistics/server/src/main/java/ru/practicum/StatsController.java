package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.InvalidTimeException;
import ru.practicum.service.StatsService;
import ru.practicum.utils.Constants;
import ru.practicum.utils.PathConstants;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @PostMapping(PathConstants.HIT)
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@Valid @RequestBody StatsRequest request) {
        log.info("Сохранение информации о посещении");
        statsService.hit(request);
    }

    @GetMapping(PathConstants.STATS)
    public List<StatsResponse> getStats(@RequestParam @NotNull
                                        @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) LocalDateTime start,
                                        @RequestParam @NotNull
                                        @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT) LocalDateTime end,
                                        @RequestParam(defaultValue = "") List<String> uris,
                                        @RequestParam(defaultValue = "false") boolean unique) {
        if (start.isAfter(end)) {
            throw new InvalidTimeException(String.format("Неправильное время: start %s не может быть позже end %s", start, end));
        }

        log.info("Получение статистики по посещениям с {} по {}", start, end);
        return statsService.stats(new GetStatsRequest(start, end, uris, unique));
    }
}