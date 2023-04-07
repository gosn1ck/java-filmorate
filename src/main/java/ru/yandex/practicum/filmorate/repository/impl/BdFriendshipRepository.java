package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("Db")
public class BdFriendshipRepository implements FriendshipRepository {

    private final JdbcTemplate jdbcTemplate;

    public BdFriendshipRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Friendship> findAll() {
        return jdbcTemplate.query(
                "select user_id, friend_id, confirmed from friends",
                this::mapRowToFriendship);
    }

    @Override
    public Optional<Friendship> findByUserAndFriendId(Integer id, Integer friendId) {
        List<Friendship> results = jdbcTemplate.query(
                "select user_id, friend_id, confirmed from friends where user_id=? and friend_id=?",
                this::mapRowToFriendship,
                id,
                friendId);
        return results.size() == 0 ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

    @Override
    public Friendship save(Friendship friendship) {
        jdbcTemplate.update(
                "INSERT INTO friends (user_id, friend_id, confirmed) values (?, ?, ?)",
                friendship.getUserId(), friendship.getFriendId(), friendship.getIsConfirmed());

        return friendship;
    }

    @Override
    public void deleteByUserAndFriendId(Integer id, Integer friendId) {
        jdbcTemplate.update(
                " DELETE FROM friends WHERE user_id=? AND friend_id=?",
                id, friendId);
    }

    private Friendship mapRowToFriendship(ResultSet row, int rowNum) throws SQLException {
        return new Friendship(
                row.getInt("user_id"),
                row.getInt("friend_id"),
                row.getBoolean("confirmed"));
    }

}
