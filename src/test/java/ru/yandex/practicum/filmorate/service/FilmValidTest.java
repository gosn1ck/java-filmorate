package ru.yandex.practicum.filmorate.service;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidTest {

    private static final String FILM_DESCRIPTION = "Chronicles the experiences of a formerly successful banker as  "
            + "a prisoner in the gloomy jailhouse of Shawshank after being found guilty of a crime"
            + "he did not commit.";

    private static final String DESCRIPTION_201_SYMBOLS = "Chronicles the experiences of a formerly successful banker as  "
            + "a prisoner in the gloomy jailhouse of Shawshank after being found guilty of a crime"
            + "he did not commit. The film portrays the man's unique w";

    private static final String DESCRIPTION_200_SYMBOLS = "Chronicles the experiences of a formerly successful banker as  "
            + "a prisoner in the gloomy jailhouse of Shawshank after being found guilty of a crime"
            + "he did not commit. The film portrays the man's unique ";

    public static final LocalDate RELEASE_DATE = LocalDate.of(1967, 3, 25);
    public static final int DURATION = 100;
    public static final String FILM_NAME = "The Shawshank Redemption";
    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @DisplayName("Фильм проходит проверку валидации")
    @Test
    void shouldAddValidFilm() {
        val film = new Film();
        film.setName(FILM_NAME);
        film.setDescription(FILM_DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(0, validates.size());

    }

    @DisplayName("Фильм не должен проходить валидацию с пустым названием")
    @Test
    void shouldNotAddFilmEmptyName() {
        val filmEmptyName = new Film();
        filmEmptyName.setName("");
        filmEmptyName.setDescription(FILM_DESCRIPTION);
        filmEmptyName.setReleaseDate(RELEASE_DATE);
        filmEmptyName.setDuration(DURATION);

        Set<ConstraintViolation<Film>> validates = validator.validate(filmEmptyName);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Name should not be empty");

        val filmNullName = new Film();
        filmNullName.setDescription(FILM_DESCRIPTION);
        filmNullName.setReleaseDate(RELEASE_DATE);
        filmNullName.setDuration(DURATION);

        validates = validator.validate(filmNullName);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(), "Name should not be empty");

    }

    @DisplayName("Фильм не должен проходить валидацию с длинным описанием")
    @Test
    void shouldNotAddFilmDescription201Symbols() {
        val film = new Film();
        film.setName(FILM_NAME);
        film.setDescription(DESCRIPTION_201_SYMBOLS);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(),
                "Description must be max 200 characters long");

    }

    @DisplayName("Фильм должен проходить валидацию с максимальной длиной описания")
    @Test
    void shouldAddFilmDescription200Symbols() {
        val film = new Film();
        film.setName(FILM_NAME);
        film.setDescription(DESCRIPTION_200_SYMBOLS);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);

        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        assertEquals(0, validates.size());

    }

    @DisplayName("Фильм не должен проходить валидацию с датой релиза до дня рождения кино")
    @Test
    void shouldNotAddFilmLessThenCinemaReleaseDate() {
        val film1890 = new Film();
        film1890.setName(FILM_NAME);
        film1890.setDescription(FILM_DESCRIPTION);
        film1890.setReleaseDate(LocalDate.of(1890, 3, 25));
        film1890.setDuration(DURATION);

        Set<ConstraintViolation<Film>> validates = validator.validate(film1890);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(),
                "release date must be over 28.12.1895");

        val film1967 = new Film();
        film1967.setName(FILM_NAME);
        film1967.setDescription(FILM_DESCRIPTION);
        film1967.setReleaseDate(LocalDate.of(1895, 12, 27));
        film1967.setDuration(DURATION);

        validates = validator.validate(film1967);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(),
                "release date must be over 28.12.1895");

    }

    @DisplayName("Фильм не должен проходить валидацию нулевой или отрицательной длительностью")
    @Test
    void shouldNotAddFilmNegativeDuration() {
        val filmNegative = new Film();
        filmNegative.setName(FILM_NAME);
        filmNegative.setDescription(FILM_DESCRIPTION);
        filmNegative.setReleaseDate(RELEASE_DATE);
        filmNegative.setDuration(-100);

        Set<ConstraintViolation<Film>> validates = validator.validate(filmNegative);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(),
                "Duration must be positive number");

        val filmZero = new Film();
        filmZero.setName(FILM_NAME);
        filmZero.setDescription(FILM_DESCRIPTION);
        filmZero.setReleaseDate(RELEASE_DATE);
        filmZero.setDuration(0);

        validates = validator.validate(filmZero);
        assertTrue(validates.size() > 0);
        assertEquals(validates.stream().findFirst().get().getMessage(),
                "Duration must be positive number");

    }

}
