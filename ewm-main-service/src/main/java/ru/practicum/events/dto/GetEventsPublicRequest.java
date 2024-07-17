package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEventsPublicRequest {
    private String text = "";
    private List<Long> categories;
    private Boolean paid;
    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime rangeEnd;
    private boolean onlyAvailable;
    private String sort;
}