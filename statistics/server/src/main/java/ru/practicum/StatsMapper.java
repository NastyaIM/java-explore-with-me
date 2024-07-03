package ru.practicum;

import org.mapstruct.Mapper;
import ru.practicum.model.Stats;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    Stats toStats(StatsRequest request);
}
