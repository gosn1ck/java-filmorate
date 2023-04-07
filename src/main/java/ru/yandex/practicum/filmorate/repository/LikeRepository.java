package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface LikeRepository {
    void save(Film film, User user);
    void delete(Film film, User user);

    List<Film> popularFilms(List<Film> films, Integer count);
}
