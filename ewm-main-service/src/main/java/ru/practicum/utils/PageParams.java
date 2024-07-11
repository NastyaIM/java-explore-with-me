package ru.practicum.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    @PositiveOrZero
    private Integer from = 0;
    @Positive
    private Integer size = 10;

    public PageRequest getPageRequest() {
        return PageRequest.of(from / size, size);
    }
}
