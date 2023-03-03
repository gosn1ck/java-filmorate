package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
public class Film {
    private Integer id;

    @NotNull
    @NotEmpty
    private String name;

    @Size(max=200, message="Description must be max 200 characters long")
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
}
