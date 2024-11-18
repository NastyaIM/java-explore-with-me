package ru.practicum.locations.adminar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.locations.dto.LocationFullDto;
import ru.practicum.locations.dto.LocationMapper;
import ru.practicum.locations.dto.NewLocationDto;
import ru.practicum.locations.dto.UpdateLocationDto;
import ru.practicum.locations.model.Location;
import ru.practicum.locations.repository.LocationRepository;
import ru.practicum.utils.GeneralMethods;
import ru.practicum.utils.PageParams;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public List<LocationFullDto> getAll(String name, Boolean saved, PageParams pageParams) {
        return locationRepository.findAll(name, saved, pageParams.getPageRequest())
                .getContent()
                .stream()
                .map(locationMapper::toLocationFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public LocationFullDto getById(long id) {
        return locationMapper.toLocationFullDto(GeneralMethods.findLocation(id, locationRepository));
    }

    @Override
    public LocationFullDto save(NewLocationDto newLocationDto) {
        try {
            Location location = locationMapper.toLocation(newLocationDto);
            location.setSaved(true);
            return locationMapper.toLocationFullDto(locationRepository.save(location));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public LocationFullDto update(long id, UpdateLocationDto updateLocationDto) {
        Location location = GeneralMethods.findLocation(id, locationRepository);
        if (updateLocationDto.getLon() != null) {
            location.setLon(updateLocationDto.getLon());
        }
        if (updateLocationDto.getLat() != null) {
            location.setLat(updateLocationDto.getLat());
        }
        if (updateLocationDto.getRadius() != null) {
            location.setRadius(updateLocationDto.getRadius());
        }
        if (updateLocationDto.getName() != null && !updateLocationDto.getName().isBlank()) {
            location.setName(updateLocationDto.getName());
        }
        if (!location.getSaved() && !location.getName().equals("User location")) {
            location.setSaved(true);
        }
        return locationMapper.toLocationFullDto(locationRepository.save(location));
    }

    @Override
    public void delete(long id) {
        GeneralMethods.findLocation(id, locationRepository);
        locationRepository.deleteById(id);
    }
}