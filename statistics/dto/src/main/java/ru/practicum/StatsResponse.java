package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {
    //@NotNull
    private String app;
    //@NotNull
    private String uri;
    private Long hits;
}
