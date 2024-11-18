package ru.practicum.locations.dto;

import org.mapstruct.Mapper;
import ru.practicum.locations.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toLocation(LocationDto location);

    LocationFullDto toLocationFullDto(Location location);

    Location toLocation(NewLocationDto newLocationDto);
}
