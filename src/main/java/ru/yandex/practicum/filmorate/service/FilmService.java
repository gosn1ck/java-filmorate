package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private Integer currentId = 0;

    @Qualifier("InMemory")
    private final FilmRepository filmRepository;
    @Qualifier("InMemory")
    private final UserRepository userRepository;

    public List<Film> getFilms() {
        return filmRepository.findAll();
    }

    public Film addFilm(Film film) {
        film.setId(nextId());
        filmRepository.save(film);
        return film;
    }

    public Optional<Film> updateFilm(Film film) {
        return filmRepository.update(film);
    }

    public Optional<Film> getFilm(Integer id) {
        return filmRepository.findById(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        var optFilm = filmRepository.findById(filmId);
        optFilm.orElseThrow(() -> new NotFoundException("film with id %d not found", filmId));

        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        optFilm.get().getLikes().add(optUser.get().getId());

    }

    public void removeLike(Integer filmId, Integer userId) {
        var optFilm = filmRepository.findById(filmId);
        optFilm.orElseThrow(() -> new NotFoundException("film with id %d not found", filmId));

        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        optFilm.get().getLikes().remove(optUser.get().getId());

    }

    public List<Film> popularFilms(Integer count) {
        var films = filmRepository.findAll();
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikes().size());
        Comparator<Film> reversed = comparator.reversed();
        return films.stream()
                .sorted(reversed)
                .limit(count)
                .collect(Collectors.toList());
    }

    private Integer nextId() {
        return ++currentId;
    }
}
