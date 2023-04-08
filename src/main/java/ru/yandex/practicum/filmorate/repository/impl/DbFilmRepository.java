package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("Db")
public class DbFilmRepository implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public DbFilmRepository(JdbcTemplate jdbcTemplate,
                            @Qualifier("Db") MpaRepository mpaRepository,
                            @Qualifier("Db") GenreRepository genreRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRepository = mpaRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(
                "SELECT film_id, film_name, description, release_date, duration, mpa_id FROM films",
                this::mapRowToFilm);
    }

    @Override
    public Optional<Film> findById(Integer id) {
        List<Film> results = jdbcTemplate.query(
                "SELECT film_id, film_name, description, release_date, duration, mpa_id FROM films WHERE film_id=?",
                this::mapRowToFilm,
                id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public Film save(Film film) {
        var optMpa = mpaRepository.findById(film.getMpa().getId());
        optMpa.orElseThrow(() -> new NotFoundException("mpa with id %d not found", film.getMpa().getId()));
        jdbcTemplate.update(
                "INSERT INTO films (film_id, film_name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?, ?)",
                film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());

        updateFilmGenres(film);
        var genres = findGenres(film.getId());
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    @Override
    public Optional<Film> update(Film film) {
        var optFilm = findById(film.getId());
        optFilm.orElseThrow(() -> new NotFoundException("film with id %d not found", film.getId()));

        var optMpa = mpaRepository.findById(film.getMpa().getId());
        optMpa.orElseThrow(() -> new NotFoundException("mpa with id %d not found", film.getMpa().getId()));

        updateFilmGenres(film);
        var genres = findGenres(film.getId());
        film.setGenres(new HashSet<>(genres));

        jdbcTemplate.update(
                "UPDATE films SET film_name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE film_id = ?",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());

        return Optional.of(film);
    }

    private void updateFilmGenres(Film film) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", film.getId());
        var genres = film.getGenres();
        genres.forEach(genre -> {
                var optGenre = genreRepository.findById(genre.getId());
                optGenre.orElseThrow(() -> new NotFoundException("genre with id %d not found", genre.getId()));
        });

        genres.forEach(genre ->
            jdbcTemplate.update(
                    "INSERT INTO film_genre (film_id, genre_id) values (?, ?)",
                    film.getId(), genre.getId()));
    }

    private Film mapRowToFilm(ResultSet row, int rowNum) throws SQLException {
        var optMpa = mpaRepository.findById(row.getInt("mpa_id"));

        var filmId = row.getInt("film_id");
        var genres = findGenres(filmId);

        var film = new Film(
                filmId,
                row.getString("film_name"),
                row.getString("description"),
                row.getDate("release_date").toLocalDate(),
                row.getInt("duration"),
                optMpa.get());
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    public List<Genre> findGenres(Integer filmId) {
        return jdbcTemplate.query(
                "SELECT g.genre_id, g.genre_name \n" +
                        "FROM film_genre AS fg \n" +
                        "  JOIN genres AS g ON g.genre_id = fg.genre_id\n" +
                        "  WHERE film_id =? ORDER BY g.genre_id",
                this::mapRowToGenre,
                filmId);
    }

    private Genre mapRowToGenre(ResultSet row, int rowNum) throws SQLException {
        return new Genre(row.getInt("genre_id"), row.getString("genre_name"));
    }

}
