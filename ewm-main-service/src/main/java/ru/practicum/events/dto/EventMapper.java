package ru.practicum.events.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.events.model.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "category.id", source = "newEventDto.category")
    Event toEvent(NewEventDto newEventDto);
    EventFullDto toEventFullDto(Event event);
    EventShortDto toEventShortDto(Event event);
}
