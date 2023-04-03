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
class GenreValidTest {

    private static final String NAME = "Action";

    @Autowired
    private Validator validator;

    @DisplayName("Жанр проходит проверку валидации")
    @Test
    void shouldAddValidGenre() {
        val genre = new Genre();
        genre.setName(NAME);

        Set<ConstraintViolation<Genre>> validates = validator.validate(genre);
        assertEquals(0, validates.size());

    }

    @DisplayName("Жанр не должен проходить валидацию с пустым названием")
    @Test
    void shouldNotAddGenreEmptyName() {
        val genreEmptyName = new Genre();
        genreEmptyName.setName("");

        Set<ConstraintViolation<Genre>> validates = validator.validate(genreEmptyName);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Name should not be empty");

        val genreNullName = new Genre();

        validates = validator.validate(genreNullName);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Name should not be empty");

    }

}