package ru.practicum.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.utils.Constants;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiError {
    private String message;
    private String reason;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
    private final LocalDateTime timestamp = LocalDateTime.now();
    private String errors;
}
