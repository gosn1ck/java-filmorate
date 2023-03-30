package ru.yandex.practicum.filmorate.model;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MpaValidTest {

    private static final String NAME = "NC-17";

    @Autowired
    private Validator validator;

    @DisplayName("Рейтинг ассоциации кинокомпаний проходит проверку валидации")
    @Test
    void shouldAddValidMpa() {
        val Mpa = new Mpa();
        Mpa.setName(NAME);

        Set<ConstraintViolation<Mpa>> validates = validator.validate(Mpa);
        assertEquals(0, validates.size());

    }

    @DisplayName("Рейтинг ассоциации кинокомпаний не должен проходить валидацию с пустым названием")
    @Test
    void shouldNotAddMpaEmptyName() {
        val mpaEmptyName = new Mpa();
        mpaEmptyName.setName("");

        Set<ConstraintViolation<Mpa>> validates = validator.validate(mpaEmptyName);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Name should not be empty");

        val mpaNullName = new Mpa();

        validates = validator.validate(mpaNullName);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Name should not be empty");

    }

}