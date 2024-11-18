package ru.practicum.locations.adminar.service;

import ru.practicum.locations.dto.LocationFullDto;
import ru.practicum.locations.dto.NewLocationDto;
import ru.practicum.locations.dto.UpdateLocationDto;
import ru.practicum.utils.PageParams;

import java.util.List;

public interface LocationService {
    List<LocationFullDto> getAll(String name, Boolean saved, PageParams pageParams);

    LocationFullDto getById(long id);

    LocationFullDto save(NewLocationDto newLocationDto);

    LocationFullDto update(long id, UpdateLocationDto updateLocationDto);

    void delete(long id);
}
