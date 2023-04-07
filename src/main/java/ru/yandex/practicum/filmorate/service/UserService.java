package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.friend.FriendManager;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private Integer currentId = 0;
    private final UserRepository userRepository;
    private final FriendManager friendManager;

    public UserService(@Qualifier("Db") UserRepository userRepository,
                       FriendManager friendManager) {
        this.userRepository = userRepository;
        this.friendManager = friendManager;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        user.setId(nextId());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return userRepository.save(user);
    }

    public Optional<User> updateUser(User user) {
        return userRepository.update(user);
    }

    public Optional<User> getUser(Integer id) {
        return userRepository.findById(id);
    }

    public void addFriend(Integer userId, Integer friendId) {
        friendManager.add(userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        friendManager.remove(userId, friendId);
    }

    public List<User> friends(Integer id) {
        return friendManager.getFriends(id);
    }

    public List<User> commonFriends(Integer userId, Integer otherId) {
        return friendManager.commonFriends(userId, otherId);
    }

    private Integer nextId() {
        return ++currentId;
    }

}
