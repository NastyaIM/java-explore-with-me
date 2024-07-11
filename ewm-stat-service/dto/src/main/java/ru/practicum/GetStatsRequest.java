package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.utils.Constants;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStatsRequest {
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime start;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime end;
    private List<String> uris;
    private boolean unique;
}
