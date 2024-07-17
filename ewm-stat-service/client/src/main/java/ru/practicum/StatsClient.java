package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.utils.Constants;
import ru.practicum.utils.PathConstants;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatsClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
    private final RestTemplate restTemplate;

    @Autowired
    public StatsClient(@Value("${STATS_SERVER_URL:http://localhost:9090}") String serverUrl, RestTemplateBuilder builder) {
        restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

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
