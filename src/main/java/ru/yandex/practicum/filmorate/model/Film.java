package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
public class Film {

    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Size(max=200, message = "Description must be max 200 characters long")
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Duration must be positive number")
    private Integer duration;
}
