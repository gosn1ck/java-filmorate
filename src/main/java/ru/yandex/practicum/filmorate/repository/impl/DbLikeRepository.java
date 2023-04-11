package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.LikeRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Qualifier("Db")
public class DbLikeRepository implements LikeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRepository filmRepository;

    public DbLikeRepository(JdbcTemplate jdbcTemplate,
                            @Qualifier("Db") FilmRepository filmRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmRepository = filmRepository;
    }

    @Override
    public void save(Film film, User user) {
        jdbcTemplate.update(
                "INSERT INTO likes (film_id, user_id) VALUES (?, ?)",
                film.getId(), user.getId());
    }

    @Override
    public void delete(Film film, User user) {
        jdbcTemplate.update(
                " DELETE FROM likes WHERE film_id=? AND user_id=?",
                film.getId(), user.getId());
    }

    @Override
    public List<Film> popularFilms(List<Film> films, Integer count) {
        List<Integer> query = jdbcTemplate.query(
                "SELECT f.FILM_ID FROM FILMS AS F LEFT JOIN LIKES AS L ON f.FILM_ID = L.FILM_ID \n"
                        + "GROUP BY f.film_id ORDER BY count(distinct L.FILM_ID) DESC LIMIT ?",
                this::mapRowToFilmId,
                count);
        return query.stream()
                .map(filmRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Integer mapRowToFilmId(ResultSet row, int rowNum) throws SQLException {
        return row.getInt("film_id");
    }
}
