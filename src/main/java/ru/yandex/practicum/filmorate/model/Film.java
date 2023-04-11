package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {

    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Size(max = 200, message = "Description must be max 200 characters long")
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Duration must be positive number")
    private Integer duration;

    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();

    private Set<Genre> genres = new HashSet<>();

    @NotNull(message = "Mpa should not be null")
    private Mpa mpa;

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}
