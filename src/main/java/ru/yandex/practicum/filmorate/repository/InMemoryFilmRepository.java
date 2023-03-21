package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("InMemory")
public class InMemoryFilmRepository implements FilmRepository {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film save(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return Optional.of(film);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Film> findById(Integer id) {
        if (films.containsKey(id)) {
            return Optional.of(films.get(id));
        }

        return Optional.empty();
    }

}
