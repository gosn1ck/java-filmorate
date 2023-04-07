package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("InMemory")
public class InMemoryGenreRepository implements GenreRepository {

    private final HashMap<Integer, Genre> genres = new HashMap<>();

    @Override
    public List<Genre> findAll() {
        return new ArrayList<>(genres.values());
    }

    @Override
    public Genre save(Genre mpa) {
        genres.put(mpa.getId(), mpa);
        return mpa;
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        if (genres.containsKey(id)) {
            return Optional.of(genres.get(id));
        }

        return Optional.empty();
    }
}
