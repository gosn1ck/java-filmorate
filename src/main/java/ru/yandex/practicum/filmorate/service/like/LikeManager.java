package ru.yandex.practicum.filmorate.service.like;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeManager {
    void add(Integer filmId, Integer userId);
    void remove(Integer filmId, Integer userId);
    List<Film> popularFilms(Integer count);
}
