package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final HashMap<Integer, User> users = new HashMap<>();
    private static Integer currentId;

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User addUser(User user) {
        user.setId(nextId());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return Optional.of(user);
        }

        return Optional.empty();
    }

    private Integer nextId() {
        if (currentId == null) {
            currentId = 0;
        }
        return ++currentId;
    }

}
