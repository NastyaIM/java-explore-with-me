package ru.practicum.utils;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.PositiveOrZero;

@AllArgsConstructor
public class PageParams {
    @PositiveOrZero
    private Integer from;
    @PositiveOrZero
    private Integer size;

    public PageRequest getPageRequest() {
        return PageRequest.of(from / size, size);
    }
}
