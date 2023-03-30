package ru.yandex.practicum.filmorate.model;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FriendValidTest {

    @Autowired
    private Validator validator;

    @DisplayName("Дружба проходит проверку валидации")
    @Test
    void shouldAddValidFriend() {
        val friendshipUnconfirmed = new Friendship();
        friendshipUnconfirmed.setUserId(1);
        friendshipUnconfirmed.setFriendId(2);
        friendshipUnconfirmed.setIsConfirmed(FALSE);

        Set<ConstraintViolation<Friendship>> validates = validator.validate(friendshipUnconfirmed);
        assertEquals(0, validates.size());

        val friendshipConfirmed = new Friendship();
        friendshipConfirmed.setUserId(2);
        friendshipConfirmed.setFriendId(3);
        friendshipConfirmed.setIsConfirmed(TRUE);

        validates = validator.validate(friendshipConfirmed);
        assertEquals(0, validates.size());

    }

    @DisplayName("Дружба не должен проходить валидацию с пустым id пользователя")
    @Test
    void shouldNotAddFriendEmptyUserId() {
        val friendship = new Friendship();
        friendship.setIsConfirmed(FALSE);
        friendship.setFriendId(1);

        Set<ConstraintViolation<Friendship>> validates = validator.validate(friendship);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "user id should not be null");

    }

    @DisplayName("Дружба не должен проходить валидацию с пустым подтверждением")
    @Test
    void shouldNotAddFriendNullConfirmed() {
        val friend = new Friendship();
        friend.setUserId(1);
        friend.setFriendId(1);

        Set<ConstraintViolation<Friendship>> validates = validator.validate(friend);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "confirmed should not be null");

    }

}