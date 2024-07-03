package ru.practicum.service;

import ru.practicum.GetStatsRequest;
import ru.practicum.StatsRequest;
import ru.practicum.StatsResponse;

import java.util.List;

public interface StatsService {
    void hit(StatsRequest request);

    List<StatsResponse> stats(GetStatsRequest request);
}