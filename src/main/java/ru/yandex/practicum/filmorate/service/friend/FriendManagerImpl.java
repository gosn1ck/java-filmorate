package ru.yandex.practicum.filmorate.service.friend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FriendManagerImpl implements FriendManager {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public FriendManagerImpl(@Qualifier("Db") UserRepository userRepository,
                             @Qualifier("Db") FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public void add(Integer userId, Integer friendId) {
        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        var optFriend = userRepository.findById(friendId);
        optFriend.orElseThrow(() -> new NotFoundException("friend with id %d not found", friendId));

        var optFriendship = friendshipRepository.findByUserAndFriendId(optUser.get().getId(), optFriend.get().getId());
        if (optFriendship.isEmpty()) {
            var friendship = new Friendship();
            friendship.setUserId(optUser.get().getId());
            friendship.setFriendId(optFriend.get().getId());
            friendship.setIsConfirmed(true);
            friendshipRepository.save(friendship);
        }

        optFriendship = friendshipRepository.findByUserAndFriendId(optFriend.get().getId(), optUser.get().getId());
        if (optFriendship.isEmpty()) {
            var friendship = new Friendship();
            friendship.setUserId(optFriend.get().getId());
            friendship.setFriendId(optUser.get().getId());
            friendship.setIsConfirmed(false);
            friendshipRepository.save(friendship);
        }
    }

    @Override
    public void remove(Integer userId, Integer friendId) {
        var optUser = userRepository.findById(userId);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", userId));

        var optFriend = userRepository.findById(friendId);
        optFriend.orElseThrow(() -> new NotFoundException("friend with id %d not found", friendId));

        friendshipRepository.deleteByUserAndFriendId(userId, friendId);
        friendshipRepository.deleteByUserAndFriendId(friendId, userId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        var optUser = userRepository.findById(id);
        optUser.orElseThrow(() -> new NotFoundException("user with id %d not found", id));

        return friendshipRepository.findAll().stream()
                .filter(friendship -> friendship.getUserId().equals(id))
                .filter(Friendship::getIsConfirmed)
                .map(Friendship::getFriendId)
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

        var common = getFriends(userId);
        var otherFriends = getFriends(otherId);
        common.retainAll(otherFriends);
        return common;
    }

}
