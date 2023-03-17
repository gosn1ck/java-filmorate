package ru.yandex.practicum.filmorate.service;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static final String USER_NAME = "Nick Name";
    private static final String FRIEND_NAME = "Friend";
    private static final String USER_LOGIN = "dolore";
    private static final String FRIEND_LOGIN = "FriendLogin";
    private static final String USER_EMAIL = "mail@mail.ru";
    private static final String FRIEND_EMAIL = "friend@mail.ru";
    private static final LocalDate USER_BIRTHDAY = LocalDate.of(1946, 8, 20);
    private static final LocalDate FRIEND_BIRTHDAY = LocalDate.of(1987, 10, 5);

    private UserService userService;

    @BeforeEach
    @Test
    void beforeEach() {
        this.userService = new UserService(new InMemoryUserRepository());
    }

    @DisplayName("Пользователь добавлен в сервис")
    @Test
    void shouldAddUser() {
        val user = new User();
        user.setName(USER_NAME);
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setBirthday(USER_BIRTHDAY);

        val savedUser = userService.addUser(user);
        assertEquals(1, savedUser.getId());
        assertEquals(user.getName(), savedUser.getName());
        assertEquals(user.getLogin(), savedUser.getLogin());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getBirthday(), savedUser.getBirthday());

        val users = userService.getUsers();
        assertEquals(1, users.size());

        val savedListUser = users.get(0);
        assertEquals(1, savedListUser.getId());
        assertEquals(user.getName(), savedListUser.getName());
        assertEquals(user.getLogin(), savedListUser.getLogin());
        assertEquals(user.getEmail(), savedListUser.getEmail());
        assertEquals(user.getBirthday(), savedListUser.getBirthday());

        val optionalUser = userService.getUser(1);
        val savedByIdUser = optionalUser.get();
        assertEquals(user.getName(), savedByIdUser.getName());
        assertEquals(user.getLogin(), savedByIdUser.getLogin());
        assertEquals(user.getEmail(), savedByIdUser.getEmail());
        assertEquals(user.getBirthday(), savedByIdUser.getBirthday());

    }

    @DisplayName("Пользователь добавлен в сервис без имени")
    @Test
    void shouldAddUserWithOutName() {
        val user = new User();
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setBirthday(USER_BIRTHDAY);

        val savedUser = userService.addUser(user);
        assertEquals(1, savedUser.getId());
        assertEquals(user.getLogin(), savedUser.getName());
        assertEquals(user.getLogin(), savedUser.getLogin());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getBirthday(), savedUser.getBirthday());

        val users = userService.getUsers();
        assertEquals(1, users.size());

        val savedListUser = users.get(0);
        assertEquals(1, savedListUser.getId());
        assertEquals(user.getLogin(), savedListUser.getName());
        assertEquals(user.getLogin(), savedListUser.getLogin());
        assertEquals(user.getEmail(), savedListUser.getEmail());
        assertEquals(user.getBirthday(), savedListUser.getBirthday());

        val optionalUser = userService.getUser(1);
        val savedByIdUser = optionalUser.get();
        assertEquals(user.getLogin(), savedByIdUser.getName());
        assertEquals(user.getLogin(), savedByIdUser.getLogin());
        assertEquals(user.getEmail(), savedByIdUser.getEmail());
        assertEquals(user.getBirthday(), savedByIdUser.getBirthday());

    }

    @DisplayName("Пользователь обновлен в сервис если он там есть")
    @Test
    void shouldUpdateUser() {
        val user = new User();
        user.setName(USER_NAME);
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setBirthday(USER_BIRTHDAY);

        userService.addUser(user);

        var newName = "new name";
        user.setName(newName);
        var newLogin = "newLogin";
        user.setLogin(newLogin);

        val optUser = userService.updateUser(user);
        val updatedUser = optUser.get();
        assertEquals(user.getId(), updatedUser.getId());
        assertEquals(user.getName(), updatedUser.getName());
        assertEquals(user.getLogin(), updatedUser.getLogin());

        val notExistingUser = new User();
        notExistingUser.setId(9999);
        notExistingUser.setName(USER_NAME);
        notExistingUser.setLogin(USER_LOGIN);
        notExistingUser.setEmail(USER_EMAIL);
        notExistingUser.setBirthday(USER_BIRTHDAY);

        val optNotExistingUser = userService.updateUser(notExistingUser);
        assertTrue(optNotExistingUser.isEmpty());

    }

    @DisplayName("Пользователя нет по несуществующему идентификатору")
    @Test
    void shouldReturnEmptyUser() {

        val optionalUser = userService.getUser(9999);
        assertTrue(optionalUser.isEmpty());

    }

    @DisplayName("Пользователи добавлены в друзья")
    @Test
    void shouldAddFriends() {
        val user = new User();
        user.setName(USER_NAME);
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setBirthday(USER_BIRTHDAY);
        userService.addUser(user);

        val friend = new User();
        friend.setName(FRIEND_NAME);
        friend.setLogin(FRIEND_LOGIN);
        friend.setEmail(FRIEND_EMAIL);
        friend.setBirthday(FRIEND_BIRTHDAY);
        userService.addUser(friend);

        val users = userService.getUsers();
        assertEquals(2, users.size());

        userService.addFriend(user.getId(), friend.getId());

        val userFriends = userService.friends(user.getId());
        assertEquals(1, userFriends.size());
        assertEquals(friend.getId(), userFriends.get(0).getId());

        val friendFriends = userService.friends(friend.getId());
        assertEquals(1, friendFriends.size());
        assertEquals(user.getId(), friendFriends.get(0).getId());

    }

    @DisplayName("Пользователи не добавлены в друзья")
    @Test
    void shouldNotAddFriendsInvalidIds() {
        val user = new User();
        user.setName(USER_NAME);
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setBirthday(USER_BIRTHDAY);
        userService.addUser(user);

        val friend = new User();
        friend.setName(FRIEND_NAME);
        friend.setLogin(FRIEND_LOGIN);
        friend.setEmail(FRIEND_EMAIL);
        friend.setBirthday(FRIEND_BIRTHDAY);
        userService.addUser(friend);

        val exceptionUser = assertThrows(NotFoundException.class, () ->
                userService.addFriend(9999, friend.getId()));
        assertEquals("user with id 9999 not found", exceptionUser.getMessage());

        val exceptionFriend = assertThrows(NotFoundException.class, () ->
                userService.addFriend(user.getId(), 9999));
        assertEquals("friend with id 9999 not found", exceptionFriend.getMessage());

    }

    @DisplayName("Пользователи удалены из друзей")
    @Test
    void shouldRemoveFriends() {
        val user = new User();
        user.setName(USER_NAME);
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setBirthday(USER_BIRTHDAY);
        userService.addUser(user);

        val friend = new User();
        friend.setName(FRIEND_NAME);
        friend.setLogin(FRIEND_LOGIN);
        friend.setEmail(FRIEND_EMAIL);
        friend.setBirthday(FRIEND_BIRTHDAY);
        userService.addUser(friend);

        val users = userService.getUsers();
        assertEquals(2, users.size());

        userService.addFriend(user.getId(), friend.getId());

        val userFriends = userService.friends(user.getId());
        assertEquals(1, userFriends.size());

        val friendFriends = userService.friends(friend.getId());
        assertEquals(1, friendFriends.size());

        userService.removeFriend(user.getId(), friend.getId());

        val emptyUserFriends = userService.friends(user.getId());
        assertEquals(0, emptyUserFriends.size());

        val emptyFriendFriends = userService.friends(friend.getId());
        assertEquals(0, emptyFriendFriends.size());

    }

    @DisplayName("Пользователи не удалены из друзей")
    @Test
    void shouldNotRemoveFriendsInvalidIds() {
        val user = new User();
        user.setName(USER_NAME);
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setBirthday(USER_BIRTHDAY);
        userService.addUser(user);

        val exceptionUser = assertThrows(NotFoundException.class, () ->
                userService.removeFriend(9999, user.getId()));
        assertEquals("user with id 9999 not found", exceptionUser.getMessage());

        val exceptionFriend = assertThrows(NotFoundException.class, () ->
                userService.removeFriend(user.getId(), 9999));
        assertEquals("friend with id 9999 not found", exceptionFriend.getMessage());

    }

    @DisplayName("Ошибка при запросе списка друзей с невалидным идентификатором")
    @Test
    void shouldNotReturnFriendsInvalidIds() {
        val exceptionUser = assertThrows(NotFoundException.class, () ->
                userService.friends(9999));
        assertEquals("user with id 9999 not found", exceptionUser.getMessage());

    }

    @DisplayName("Пользователи имеют общий список друзей")
    @Test
    void shouldHaveCommonFriends() {
        val user = new User();
        user.setName(USER_NAME);
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setBirthday(USER_BIRTHDAY);
        userService.addUser(user);

        val friend = new User();
        friend.setName(FRIEND_NAME);
        friend.setLogin(FRIEND_LOGIN);
        friend.setEmail(FRIEND_EMAIL);
        friend.setBirthday(FRIEND_BIRTHDAY);
        userService.addUser(friend);

        val commonFriend = new User();
        commonFriend.setName("Common friend");
        commonFriend.setLogin("Common login");
        commonFriend.setEmail("common@mail.ru");
        commonFriend.setBirthday(LocalDate.of(2000, 1, 1));
        userService.addUser(commonFriend);

        val users = userService.getUsers();
        assertEquals(3, users.size());

        var common = userService.commonFriends(user.getId(), friend.getId());
        assertEquals(0, common.size());

        userService.addFriend(user.getId(), friend.getId());
        userService.addFriend(user.getId(), commonFriend.getId());

        val userFriends = userService.friends(user.getId());
        assertEquals(2, userFriends.size());

        userService.addFriend(friend.getId(), commonFriend.getId());

        val friendFriends = userService.friends(user.getId());
        assertEquals(2, friendFriends.size());

        common = userService.commonFriends(user.getId(), friend.getId());
        assertEquals(1, common.size());
        assertEquals(commonFriend.getId(), common.get(0).getId());
        assertEquals(commonFriend.getName(), common.get(0).getName());

    }

    @DisplayName("Ошибка при запросе общего списка друзей с невалидным идентификатором")
    @Test
    void shouldNotReturnCommonFriendsInvalidIds() {
        val user = new User();
        user.setName(USER_NAME);
        user.setLogin(USER_LOGIN);
        user.setEmail(USER_EMAIL);
        user.setBirthday(USER_BIRTHDAY);
        userService.addUser(user);

        val exceptionUser = assertThrows(NotFoundException.class, () ->
                userService.commonFriends(9999, user.getId()));
        assertEquals("user with id 9999 not found", exceptionUser.getMessage());

        val exceptionFriend = assertThrows(NotFoundException.class, () ->
                userService.commonFriends(user.getId(), 9999));
        assertEquals("man with id 9999 not found", exceptionFriend.getMessage());

    }

}
