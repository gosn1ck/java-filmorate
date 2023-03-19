package ru.yandex.practicum.filmorate.service.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Qualifier("InMemory")
public class InMemoryFriendManager implements FriendManager {

    @Qualifier("InMemory")
    private final UserRepository userRepository;

    @Override
    public void add(Integer userId, Integer friendId) {
        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        var optFriend = userRepository.findById(friendId);
        optFriend.orElseThrow(() -> new NotFoundException("friend with id %d not found", friendId));

        optUser.get().getFriends().add(optFriend.get().getId());
        optFriend.get().getFriends().add(optUser.get().getId());
    }

    @Override
    public void remove(Integer userId, Integer friendId) {
        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        var optFriend = userRepository.findById(friendId);
        optFriend.orElseThrow(() -> new NotFoundException("friend with id %d not found", friendId));

        optUser.get().getFriends().remove(optFriend.get().getId());
        optFriend.get().getFriends().remove(optUser.get().getId());
    }

    @Override
    public List<User> getFriends(Integer id) {
        var optUser = userRepository.findById(id);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", id));

        var user = optUser.get();
        return user.getFriends().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
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

}
