package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.utils.Constants;
import ru.practicum.utils.PathConstants;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
    private final RestTemplate restTemplate;

    public void hit(StatsRequest request) {
        restTemplate.postForEntity(PathConstants.HIT, request, Void.class);
    }

    public List<StatsResponse> stats(GetStatsRequest request) {
        String start = request.getStart().format(formatter);
        String end = request.getEnd().format(formatter);
        StringBuilder path = new StringBuilder("/stats?start=" + start + "&end=" + end);
        if (!request.getUris().isEmpty()) {
            path.append("&uri=");
            path.append(String.join(",", request.getUris()));
        }
        if (request.isUnique()) {
            path.append("&unique=" + true);
        }
        return restTemplate.exchange(path.toString(),
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<StatsResponse>>() {
                }).getBody();
    }
}
