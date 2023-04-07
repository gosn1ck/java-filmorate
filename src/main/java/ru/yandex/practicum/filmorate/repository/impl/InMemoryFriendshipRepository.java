package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("InMemory")
public class InMemoryFriendshipRepository implements FriendshipRepository {
    private final List<Friendship> friendships = new ArrayList<>();

    @Override
    public List<Friendship> findAll() {
        return friendships;
    }

    @Override
    public Friendship save(Friendship friendship) {
        friendships.add(friendship);
        return friendship;
    }

    @Override
    public Optional<Friendship> findByUserAndFriendId(Integer id, Integer friendId) {
        return friendships.stream()
                .filter(user -> user.getUserId().equals(id))
                .filter(friend -> friend.getFriendId().equals(friendId))
                .findFirst();
    }

    @Override
    public void deleteByUserAndFriendId(Integer id, Integer friendId) {
        friendships.removeIf(user -> user.getUserId().equals(id) && user.getFriendId().equals(friendId));
    }
}
