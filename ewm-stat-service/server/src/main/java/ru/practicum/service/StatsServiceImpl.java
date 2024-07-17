package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.GetStatsRequest;
import ru.practicum.StatsMapper;
import ru.practicum.StatsRequest;
import ru.practicum.StatsResponse;
import ru.practicum.repository.StatsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final StatsMapper mapper;

    @Override
    public void hit(StatsRequest request) {
        statsRepository.save(mapper.toStats(request));
    }

    @Override
    public List<StatsResponse> stats(GetStatsRequest request) {
        if (request.isUnique()) {
            if (request.getUris().isEmpty()) {
                return statsRepository.findStatsUniqueIp(request.getStart(), request.getEnd());
            }
            return statsRepository.findStatsUniqueIpByUri(request.getUris(),
                    request.getStart(), request.getEnd());
        } else {
            if (request.getUris().isEmpty()) {
                return statsRepository.findStats(request.getStart(), request.getEnd());
            }
            return statsRepository.findStatsByUri(request.getUris(),
                    request.getStart(), request.getEnd());
        }
    }
}