package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Qualifier("Db")
public class DbGenreRepository implements GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    public DbGenreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT genre_id, genre_name FROM genres", this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        List<Genre> results = jdbcTemplate.query("SELECT genre_id, genre_name FROM genres WHERE genre_id = ?",
                this::mapRowToGenre,
                id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));

    }

    @Override
    public Genre save(Genre genre) {
        jdbcTemplate.update("INSERT INTO genres (genre_id, genre_name) VALUES (?, ?)", genre.getId(), genre.getName());
        return genre;
    }

    @Override
    public List<Genre> findAllById(List<Integer> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>();
        }

        String inParams = ids.stream().map(id -> "?").collect(Collectors.joining(","));
        return jdbcTemplate.query(
                String.format("SELECT genre_id, genre_name FROM genres WHERE genre_id IN (%s)", inParams),
                ids.toArray(),
                this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet row, int rowNum) throws SQLException {
        return new Genre(row.getInt("genre_id"), row.getString("genre_name"));
    }

}
