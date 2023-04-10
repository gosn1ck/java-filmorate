package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    List<Film> findAll();

    Optional<Film> findById(Integer id);

    Film save(Film film);

    Optional<Film> update(Film film);

}
