package ru.practicum.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewLocationDto {
    @NotNull
    @Min(-90)
    @Max(90)
    private Double lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private Double lon;
    @PositiveOrZero
    private double radius;
    @NotBlank
    @Size(min = 1, max = 120)
    private String name;
}
