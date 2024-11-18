package ru.practicum.locations.adminar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.locations.adminar.service.LocationService;
import ru.practicum.locations.dto.LocationFullDto;
import ru.practicum.locations.dto.NewLocationDto;
import ru.practicum.locations.dto.UpdateLocationDto;
import ru.practicum.utils.PageParams;
import ru.practicum.utils.PathConstants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(PathConstants.ADMIN + PathConstants.LOCATIONS)
@RequiredArgsConstructor
@Slf4j
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public List<LocationFullDto> geAll(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) Boolean saved,
                                       @Valid PageParams pageParams) {
        log.info("Получение списка всех локаций с возможностью фильтрации");
        return locationService.getAll(name, saved, pageParams);
    }

    @GetMapping(PathConstants.BY_ID)
    public LocationFullDto getById(@PathVariable long id) {
        log.info("Получение локации с id {}", id);
        return locationService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationFullDto save(@Valid @RequestBody NewLocationDto newLocationDto) {
        log.info("Сохранение новой локации {}", newLocationDto);
        return locationService.save(newLocationDto);
    }

    @PatchMapping(PathConstants.BY_ID)
    public LocationFullDto update(@PathVariable long id,
                                  @Valid @RequestBody UpdateLocationDto updateLocationDto) {
        log.info("Обновление локации с id {}", id);
        return locationService.update(id, updateLocationDto);
    }

    @DeleteMapping(PathConstants.BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("Удаление локации с id {}", id);
        locationService.delete(id);
    }
}