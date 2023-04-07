package ru.yandex.practicum.filmorate.service.like;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.LikeRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LikeManagerImpl implements LikeManager {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public LikeManagerImpl(@Qualifier("Db") FilmRepository filmRepository,
                           @Qualifier("Db") UserRepository userRepository,
                           @Qualifier("Db") LikeRepository likeRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public void add(Integer filmId, Integer userId) {
        var optFilm = filmRepository.findById(filmId);
        optFilm.orElseThrow(() -> new NotFoundException("film with id %d not found", filmId));

        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        likeRepository.save(optFilm.get(), optUser.get());
    }

    @Override
    public void remove(Integer filmId, Integer userId) {
        var optFilm = filmRepository.findById(filmId);
        optFilm.orElseThrow(() -> new NotFoundException("film with id %d not found", filmId));

        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        likeRepository.delete(optFilm.get(), optUser.get());
    }

    @Override
    public List<Film> popularFilms(Integer count) {
        var films = filmRepository.findAll();
        return likeRepository.popularFilms(films, count);

    }

}
