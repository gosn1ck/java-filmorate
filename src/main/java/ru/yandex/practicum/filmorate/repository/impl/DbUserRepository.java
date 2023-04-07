package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("Db")
public class DbUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public DbUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "select user_id, user_name, email, login, birthday from users",
                this::mapRowToUser);
    }

    @Override
    public Optional<User> findById(Integer id) {
        List<User> results = jdbcTemplate.query(
                "select user_id, user_name, email, login, birthday from users where user_id=?",
                this::mapRowToUser,
                id);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public User save(User user) {
        jdbcTemplate.update(
                "INSERT INTO users (user_id, user_name, email, login, birthday) values (?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());

        return user;
    }

    @Override
    public Optional<User> update(User user) {
        var optUser = findById(user.getId());
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", user.getId()));

        jdbcTemplate.update(
                "update users set user_name = ?, email = ?, login = ?, birthday = ? where user_id  = ?",
                user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());

        return Optional.of(user);

    }

    private User mapRowToUser(ResultSet row, int rowNum) throws SQLException {
        return new User(
                row.getInt("user_id"),
                row.getString("user_name"),
                row.getString("login"),
                row.getString("email"),
                row.getDate("birthday").toLocalDate());
    }

}
