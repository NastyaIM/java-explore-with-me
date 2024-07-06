package ru.practicum.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {
    @PositiveOrZero
    private Integer from;
    @PositiveOrZero
    private Integer size;
}
