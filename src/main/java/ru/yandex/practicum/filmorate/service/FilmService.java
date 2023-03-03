package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private static Integer currentId;
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film addFilm(Film film) {
        film.setId(nextId());
        films.put(film.getId(), film);
        return film;
    }

    public Optional<Film> updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return Optional.of(film);
        }

        return Optional.empty();
    }

    private Integer nextId() {
        if (currentId == null) {
            currentId = 0;
        }
        return ++currentId;
    }
}
