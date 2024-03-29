package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.like.LikeManager;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private Integer currentId = 0;

    private final FilmRepository filmRepository;
    private final LikeManager likeManager;

    public FilmService(@Qualifier("Db") FilmRepository filmRepository, LikeManager likeManager) {
        this.filmRepository = filmRepository;
        this.likeManager = likeManager;
    }

    public List<Film> getFilms() {
        return filmRepository.findAll();
    }

    public Film addFilm(Film film) {
        film.setId(nextId());
        return filmRepository.save(film);
    }

    public Optional<Film> updateFilm(Film film) {
        return filmRepository.update(film);
    }

    public Optional<Film> getFilm(Integer id) {
        return filmRepository.findById(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        likeManager.add(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        likeManager.remove(filmId, userId);
    }

    public List<Film> popularFilms(Integer count) {
        return likeManager.popularFilms(count);
    }

    private Integer nextId() {
        return ++currentId;
    }

}
