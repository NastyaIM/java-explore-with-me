package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {
    //private Long id;
    private String app;
    private String uri;
    private Long hits;
}
