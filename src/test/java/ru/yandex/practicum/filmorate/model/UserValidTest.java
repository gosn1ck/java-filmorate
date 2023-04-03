package ru.yandex.practicum.filmorate.model;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserValidTest {

    private static final String NAME = "Nick Name";
    private static final String LOGIN = "dolore";
    private static final String EMAIL = "mail@mail.ru";
    private static final LocalDate BIRTHDAY = LocalDate.of(1946, 8, 20);

    @Autowired
    private Validator validator;

    @DisplayName("Пользователь проходит проверку валидации")
    @Test
    void shouldAddValidUser() {
        val user = new User();
        user.setName(NAME);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setBirthday(BIRTHDAY);

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertEquals(0, validates.size());

    }

    @DisplayName("Пользователь не должен проходить валидацию с не корректным email")
    @Test
    void shouldNotAddUserInvalidEmail() {
        val userNoAt = new User();
        userNoAt.setName(NAME);
        userNoAt.setLogin(LOGIN);
        userNoAt.setEmail("mail.ru");
        userNoAt.setBirthday(BIRTHDAY);

        Set<ConstraintViolation<User>> validates = validator.validate(userNoAt);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Email is not valid");

        val userAtLastChar = new User();
        userAtLastChar.setName(NAME);
        userAtLastChar.setLogin(LOGIN);
        userAtLastChar.setEmail("mail.ru@");
        userAtLastChar.setBirthday(BIRTHDAY);

        validates = validator.validate(userAtLastChar);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Email is not valid");

        val userEmptyEmail = new User();
        userEmptyEmail.setName(NAME);
        userEmptyEmail.setLogin(LOGIN);
        userEmptyEmail.setEmail("");
        userEmptyEmail.setBirthday(BIRTHDAY);

        validates = validator.validate(userEmptyEmail);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Email should not be empty");

        val userNullEmail = new User();
        userNullEmail.setName(NAME);
        userNullEmail.setLogin(LOGIN);
        userNullEmail.setEmail("");
        userNullEmail.setBirthday(BIRTHDAY);

        validates = validator.validate(userNullEmail);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Email should not be empty");

    }

    @DisplayName("Пользователь не должен проходить валидацию с не корректный логином")
    @Test
    void shouldNotAddUserInvalidLogin() {
        val userSpaceLogin = new User();
        userSpaceLogin.setName(NAME);
        userSpaceLogin.setLogin("dolore ullamco");
        userSpaceLogin.setEmail(EMAIL);
        userSpaceLogin.setBirthday(BIRTHDAY);

        Set<ConstraintViolation<User>> validates = validator.validate(userSpaceLogin);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Login should not contains space");

        val userEmptyLogin = new User();
        userEmptyLogin.setName(NAME);
        userEmptyLogin.setLogin("");
        userEmptyLogin.setEmail(EMAIL);
        userEmptyLogin.setBirthday(BIRTHDAY);

        validates = validator.validate(userEmptyLogin);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream()
                        .filter(v -> v.getMessage().equals("Login should not contains space"))
                        .findFirst().get().getMessage(), "Login should not contains space");

    }

    @DisplayName("Пользователь не должен проходить валидацию c будущей дату рождения")
    @Test
    void shouldNotAddUserInvalidDateBirth() {
        val userFuture = new User();
        userFuture.setName(NAME);
        userFuture.setLogin(LOGIN);
        userFuture.setEmail(EMAIL);
        userFuture.setBirthday(LocalDate.of(2100, 1, 1));

        Set<ConstraintViolation<User>> validates = validator.validate(userFuture);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(),
                "Birthday should be in the past");

        val userTomorrow = new User();
        userTomorrow.setName(NAME);
        userTomorrow.setLogin(LOGIN);
        userTomorrow.setEmail(EMAIL);
        userTomorrow.setBirthday(LocalDate.now().plusDays(1));

        validates = validator.validate(userTomorrow);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(),
                "Birthday should be in the past");

    }

}
