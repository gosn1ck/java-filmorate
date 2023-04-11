package ru.yandex.practicum.filmorate.repository.impl;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FriendshipRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({BdFriendshipRepository.class, DbUserRepository.class})
class BdFriendshipRepositoryTest {

    @Autowired
    @Qualifier("Db")
    private FriendshipRepository underTest;

    @Autowired
    @Qualifier("Db")
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        val user = new User();
        user.setId(1);
        user.setName("Nick Name");
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        userRepository.save(user);

        val friend = new User();
        friend.setId(2);
        friend.setName("friend");
        friend.setLogin("friend");
        friend.setEmail("friend@mail.ru");
        friend.setBirthday(LocalDate.of(1976, 8, 18));
        userRepository.save(friend);
    }

    @DisplayName("Дружба сохраняется")
    @Test
    public void shouldSaveFriendship() {

        val shouldBeEmpty = underTest.findAll();
        assertEquals(shouldBeEmpty.size(), 0);

        val friendship = new Friendship();
        friendship.setUserId(1);
        friendship.setFriendId(2);
        friendship.setIsConfirmed(TRUE);

        underTest.save(friendship);

        val savedFriendship = underTest.findAll();
        assertEquals(savedFriendship.size(), 1);
        val savedFriendshipFromList = savedFriendship.get(0);
        assertEquals(savedFriendshipFromList.getUserId(), friendship.getUserId());
        assertEquals(savedFriendshipFromList.getFriendId(), friendship.getFriendId());
        assertEquals(savedFriendshipFromList.getIsConfirmed(), friendship.getIsConfirmed());

        val optFriendship = underTest.findByUserAndFriendId(friendship.getUserId(), friendship.getFriendId());
        assertTrue(optFriendship.isPresent());
        val savedFriendshipById = optFriendship.get();
        assertEquals(savedFriendshipById.getUserId(), friendship.getUserId());
        assertEquals(savedFriendshipById.getFriendId(), friendship.getFriendId());
        assertEquals(savedFriendshipById.getIsConfirmed(), friendship.getIsConfirmed());

    }

    @DisplayName("Дружба удаляется")
    @Test
    public void shouldDeleteFriendship() {

        val friendship = new Friendship();
        friendship.setUserId(1);
        friendship.setFriendId(2);
        friendship.setIsConfirmed(TRUE);

        underTest.save(friendship);

        val savedFriendship = underTest.findAll();
        assertEquals(savedFriendship.size(), 1);

        underTest.deleteByUserAndFriendId(friendship.getUserId(), friendship.getFriendId());
        val savedFriendshipAfterRemove = underTest.findAll();
        assertEquals(savedFriendshipAfterRemove.size(), 0);

    }

}