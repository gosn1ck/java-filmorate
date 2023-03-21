package ru.yandex.practicum.filmorate.service.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendManager {
    void add(Integer userId, Integer friendId);
    void remove(Integer userId, Integer friendId);
    List<User> getFriends(Integer id);
    List<User> commonFriends(Integer userId, Integer otherId);
}
