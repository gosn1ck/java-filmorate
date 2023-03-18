package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private Integer currentId = 0;

    @Qualifier("InMemory")
    private final UserRepository userRepository;

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
        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        var optFriend = userRepository.findById(friendId);
        optFriend.orElseThrow(() -> new NotFoundException("friend with id %d not found", friendId));

        optUser.get().getFriends().add(optFriend.get().getId());
        optFriend.get().getFriends().add(optUser.get().getId());
    }

    public void removeFriend(Integer userId, Integer friendId) {
        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        var optFriend = userRepository.findById(friendId);
        optFriend.orElseThrow(() -> new NotFoundException("friend with id %d not found", friendId));

        optUser.get().getFriends().remove(optFriend.get().getId());
        optFriend.get().getFriends().remove(optUser.get().getId());
    }

    public List<User> friends(Integer id) {
        var optUser = userRepository.findById(id);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", id));

        var user = optUser.get();
        return user.getFriends().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<User> commonFriends(Integer userId, Integer otherId) {
        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        var optOther = userRepository.findById(otherId);
        optOther.orElseThrow(() -> new NotFoundException("man with id %d not found", otherId));

        var common = optUser.get().getFriends().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        var otherFriends = optOther.get().getFriends().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        common.retainAll(otherFriends);
        return common;
    }

    private Integer nextId() {
        return ++currentId;
    }

}
