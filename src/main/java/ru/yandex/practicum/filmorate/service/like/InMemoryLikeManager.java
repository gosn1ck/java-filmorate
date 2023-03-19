package ru.yandex.practicum.filmorate.service.like;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Qualifier("InMemory")
public class InMemoryLikeManager implements LikeManager {

    @Qualifier("InMemory")
    private final FilmRepository filmRepository;
    @Qualifier("InMemory")
    private final UserRepository userRepository;
    @Override
    public void add(Integer filmId, Integer userId) {
        var optFilm = filmRepository.findById(filmId);
        optFilm.orElseThrow(() -> new NotFoundException("film with id %d not found", filmId));

        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        optFilm.get().getLikes().add(optUser.get().getId());
    }

    @Override
    public void remove(Integer filmId, Integer userId) {
        var optFilm = filmRepository.findById(filmId);
        optFilm.orElseThrow(() -> new NotFoundException("film with id %d not found", filmId));

        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        optFilm.get().getLikes().remove(optUser.get().getId());
    }

    @Override
    public List<Film> popularFilms(Integer count) {
        var films = filmRepository.findAll();
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikes().size());
        Comparator<Film> reversed = comparator.reversed();
        return films.stream()
                .sorted(reversed)
                .limit(count)
                .collect(Collectors.toList());
    }

}
