package ru.practicum.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationFullDto {
    private Long id;
    private double lat;
    private double lon;
    private double radius;
    private String name;
    private boolean saved;
}