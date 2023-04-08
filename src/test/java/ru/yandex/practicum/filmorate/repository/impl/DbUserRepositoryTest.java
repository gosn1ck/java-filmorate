package ru.yandex.practicum.filmorate.repository.impl;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(DbUserRepository.class)
class DbUserRepositoryTest {

    private static final String NAME = "Nick Name";
    private static final String LOGIN = "dolore";
    private static final String EMAIL = "mail@mail.ru";
    private static final LocalDate BIRTHDAY = LocalDate.of(1946, 8, 20);

    @Autowired
    @Qualifier("Db")
    private UserRepository underTest;

    @DisplayName("Пользователь сохраняется")
    @Test
    public void shouldSaveUser() {

        val shouldBeEmpty = underTest.findAll();
        assertEquals(shouldBeEmpty.size(), 0);

        val id = 1;
        val user = new User();
        user.setId(id);
        user.setName(NAME);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setBirthday(BIRTHDAY);

        underTest.save(user);

        val savedOptUser = underTest.findById(user.getId());
        assertTrue(savedOptUser.isPresent());
        val savedUser = savedOptUser.get();
        assertEquals(savedUser.getId(), id);
        assertEquals(savedUser.getName(), user.getName());
        assertEquals(savedUser.getLogin(), user.getLogin());
        assertEquals(savedUser.getEmail(), user.getEmail());
        assertEquals(savedUser.getBirthday(), user.getBirthday());

        val savedUsers = underTest.findAll();
        assertEquals(savedUsers.size(), 1);
        val savedUserFromList = savedUsers.get(0);
        assertEquals(savedUserFromList.getId(), id);
        assertEquals(savedUserFromList.getName(), user.getName());
        assertEquals(savedUserFromList.getLogin(), user.getLogin());
        assertEquals(savedUserFromList.getEmail(), user.getEmail());
        assertEquals(savedUserFromList.getBirthday(), user.getBirthday());

    }

    @DisplayName("Не возвращается пользователь по не существующему идентификатору 9999")
    @Test
    public void shouldNotFindFilmById() {
        val optUser = underTest.findById(9999);
        assertFalse(optUser.isPresent());
    }

    @DisplayName("Пользователь обновляется")
    @Test
    public void shouldUpdateFilm() {

        val id = 1;
        val user = new User();
        user.setId(id);
        user.setName(NAME);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setBirthday(BIRTHDAY);

        underTest.save(user);

        val updatedName = "est adipisicing";
        val updatedLogin = "doloreUpdate";
        val updatedEmail = "mail@yandex.ru";
        val updatedBirthday = LocalDate.of(1989, 4, 17);
        user.setName(updatedName);
        user.setLogin(updatedLogin);
        user.setEmail(updatedEmail);
        user.setBirthday(updatedBirthday);

        underTest.update(user);
        val savedOptUser = underTest.findById(id);
        assertTrue(savedOptUser.isPresent());
        val savedUser = savedOptUser.get();
        assertEquals(savedUser.getId(), id);
        assertEquals(savedUser.getName(), updatedName);
        assertEquals(savedUser.getLogin(), updatedLogin);
        assertEquals(savedUser.getBirthday(), updatedBirthday);
        assertEquals(savedUser.getEmail(), updatedEmail);

        val savedUsers = underTest.findAll();
        assertEquals(savedUsers.size(), 1);
        val savedUserFromList = savedUsers.get(0);
        assertEquals(savedUserFromList.getId(), id);
        assertEquals(savedUserFromList.getName(), updatedName);
        assertEquals(savedUserFromList.getLogin(), updatedLogin);
        assertEquals(savedUserFromList.getBirthday(), updatedBirthday);
        assertEquals(savedUserFromList.getEmail(), updatedEmail);

    }

}