package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("InMemory")
public class InMemoryUserRepository implements UserRepository {

    private final HashMap<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Integer id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }

        return Optional.empty();
    }
}
