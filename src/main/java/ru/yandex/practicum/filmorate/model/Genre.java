package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Genre {
    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    public Genre() {
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
