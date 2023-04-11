package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.LikeRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Qualifier("InMemory")
public class InMemoryLikeRepository implements LikeRepository {

    @Override
    public void save(Film film, User user) {
        film.getLikes().add(user.getId());
    }

    @Override
    public void delete(Film film, User user) {
        film.getLikes().remove(user.getId());
    }

    @Override
    public List<Film> popularFilms(List<Film> films, Integer count) {
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikes().size());
        Comparator<Film> reversed = comparator.reversed();
        return films.stream()
                .sorted(reversed)
                .limit(count)
                .collect(Collectors.toList());
    }
}
