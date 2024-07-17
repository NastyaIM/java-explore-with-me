package ru.practicum.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEventByLocationRequest {
    @Positive
    private Long id;
    @Min(-90)
    @Max(90)
    private Double lat;
    @Min(-180)
    @Max(180)
    private Double lon;
}
