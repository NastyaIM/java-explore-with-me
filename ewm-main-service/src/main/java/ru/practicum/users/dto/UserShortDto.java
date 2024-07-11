package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    private Long id;
    //    @NotBlank
//    @Size(min = 2, max = 250)
    private String name;
}
