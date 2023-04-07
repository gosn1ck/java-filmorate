package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("Db")
public class DbGenreRepository implements GenreRepository {

    private final JdbcTemplate jdbcTemplate;

    public DbGenreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("select genre_id, genre_name from genres", this::mapRowToGenre);
    }

    @Override
    public Optional<Genre> findById(Integer id) {
        List<Genre> results = jdbcTemplate.query("select genre_id, genre_name from genres where genre_id=?",
                this::mapRowToGenre,
                id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));

    }

    @Override
    public Genre save(Genre genre) {
        jdbcTemplate.update("INSERT INTO mpas (?, ?)", genre.getId(), genre.getName());
        return genre;
    }

    private Genre mapRowToGenre(ResultSet row, int rowNum) throws SQLException {
        return new Genre(row.getInt("genre_id"), row.getString("genre_name"));
    }

}
