package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class Mpa {
    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    private String name;
}
