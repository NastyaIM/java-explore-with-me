package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.utils.Constants;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    private Long category;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    @NotNull
    @Valid
    private LocationDto location;
    @PositiveOrZero
    private int participantLimit;
    private boolean requestModeration = true;
    private boolean paid;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}