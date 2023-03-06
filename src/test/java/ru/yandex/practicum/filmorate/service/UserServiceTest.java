package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static final Validator validator;
    public static final String NAME = "Nick Name";
    public static final String LOGIN = "dolore";
    public static final String EMAIL = "mail@mail.ru";
    public static final LocalDate BIRTHDAY = LocalDate.of(1946, 8, 20);

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @DisplayName("Пользователь проходит проверку валидации")
    @Test
    void shouldAddValidUser() {
        final var user = new User();
        user.setName(NAME);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setBirthday(BIRTHDAY);

        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertTrue(validates.size() == 0);

    }

    @DisplayName("Пользователь не должен проходить валидацию с не корректным email")
    @Test
    void shouldNotAddUserInvalidEmail() {
        final var userNoAt = new User();
        userNoAt.setName(NAME);
        userNoAt.setLogin(LOGIN);
        userNoAt.setEmail("mail.ru");
        userNoAt.setBirthday(BIRTHDAY);

        Set<ConstraintViolation<User>> validates = validator.validate(userNoAt);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Email is not valid");

        final var userAtLastChar = new User();
        userAtLastChar.setName(NAME);
        userAtLastChar.setLogin(LOGIN);
        userAtLastChar.setEmail("mail.ru@");
        userAtLastChar.setBirthday(BIRTHDAY);

        validates = validator.validate(userAtLastChar);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Email is not valid");

        final var userEmptyEmail = new User();
        userEmptyEmail.setName(NAME);
        userEmptyEmail.setLogin(LOGIN);
        userEmptyEmail.setEmail("");
        userEmptyEmail.setBirthday(BIRTHDAY);

        validates = validator.validate(userEmptyEmail);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Email should not be empty");

        final var userNullEmail = new User();
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
        final var userSpaceLogin = new User();
        userSpaceLogin.setName(NAME);
        userSpaceLogin.setLogin("dolore ullamco");
        userSpaceLogin.setEmail(EMAIL);
        userSpaceLogin.setBirthday(BIRTHDAY);

        Set<ConstraintViolation<User>> validates = validator.validate(userSpaceLogin);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Login should not contains space");

        final var userEmptyLogin = new User();
        userEmptyLogin.setName(NAME);
        userEmptyLogin.setLogin("");
        userEmptyLogin.setEmail(EMAIL);
        userEmptyLogin.setBirthday(BIRTHDAY);

        validates = validator.validate(userEmptyLogin);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Login should not contains space");

    }

    @DisplayName("Пользователь не должен проходить валидацию c будущей дату рождения")
    @Test
    void shouldNotAddUserInvalidDateBirth() {
        final var userFuture = new User();
        userFuture.setName(NAME);
        userFuture.setLogin(LOGIN);
        userFuture.setEmail(EMAIL);
        userFuture.setBirthday(LocalDate.of(2100, 1, 1));

        Set<ConstraintViolation<User>> validates = validator.validate(userFuture);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(),
                "Birthday should be in the past");

        final var userTomorrow = new User();
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