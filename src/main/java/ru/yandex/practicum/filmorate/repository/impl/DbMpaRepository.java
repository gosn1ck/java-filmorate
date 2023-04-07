package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("Db")
public class DbMpaRepository implements MpaRepository {

    private final JdbcTemplate jdbcTemplate;

    public DbMpaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query("select mpa_id, mpa_name from mpas", this::mapRowToMpa);
    }

    @Override
    public Optional<Mpa> findById(Integer id) {
        List<Mpa> results = jdbcTemplate.query("select mpa_id, mpa_name from mpas where mpa_id=?",
                this::mapRowToMpa,
                id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));

    }

    @Override
    public Mpa save(Mpa mpa) {
        jdbcTemplate.update("INSERT INTO mpas (?, ?)", mpa.getId(), mpa.getName());
        return mpa;
    }

    private Mpa mapRowToMpa(ResultSet row, int rowNum) throws SQLException {
        return new Mpa(row.getInt("mpa_id"), row.getString("mpa_name"));
    }

}
