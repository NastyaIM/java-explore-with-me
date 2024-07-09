package ru.practicum.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
    @NotBlank
    @Size(min = 6, max = 254)
    @Email
    private String email;
}
