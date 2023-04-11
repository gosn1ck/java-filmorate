package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository {
    List<Friendship> findAll();

    Optional<Friendship> findByUserAndFriendId(Integer id, Integer friendId);

    Friendship save(Friendship friendship);

    void deleteByUserAndFriendId(Integer id, Integer friendId);

}
