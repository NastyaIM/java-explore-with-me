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
public class GetEventAdminRequest {
    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime rangeEnd;
}
