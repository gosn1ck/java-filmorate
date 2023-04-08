package ru.yandex.practicum.filmorate.repository.impl;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({DbLikeRepository.class, DbFilmRepository.class, DbUserRepository.class, DbMpaRepository.class,
        DbGenreRepository.class})
class DbLikeRepositoryTest {

    @Autowired
    @Qualifier("Db")
    private LikeRepository underTest;
    @Autowired
    @Qualifier("Db")
    private FilmRepository filmRepository;
    @Autowired
    @Qualifier("Db")
    private UserRepository userRepository;
    @Autowired
    @Qualifier("Db")
    private MpaRepository mpaRepository;
    @Autowired
    @Qualifier("Db")
    private GenreRepository genreRepository;

    private static final String NAME = "Nick Name";
    private static final String LOGIN = "dolore";
    private static final String EMAIL = "mail@mail.ru";
    private static final LocalDate BIRTHDAY = LocalDate.of(1946, 8, 20);

    private static final String FILM_DESCRIPTION = "Chronicles the experiences of a formerly successful banker as  "
            + "a prisoner in the gloomy jailhouse of Shawshank after being found guilty of a crime"
            + "he did not commit.";

    private static final LocalDate RELEASE_DATE = LocalDate.of(1967, 3, 25);
    private static final int DURATION = 100;

    private static final Mpa MPA;

    private static final String FILM_NAME = "The Shawshank Redemption";

    static {
        MPA = new Mpa();
        MPA.setId(1);
        MPA.setName("PG-13");
    }

    @BeforeEach
    public void beforeEach() {

        val user = new User();
        user.setId(1);
        user.setName(NAME);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setBirthday(BIRTHDAY);
        userRepository.save(user);

        val film = new Film();
        film.setId(1);
        film.setName(FILM_NAME);
        film.setDescription(FILM_DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        film.setMpa(MPA);

        filmRepository.save(film);
    }

    @DisplayName("Лайк сохраняется")
    @Test
    public void shouldSaveLike() {

        val user = new User();
        user.setId(1);
        user.setName(NAME);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setBirthday(BIRTHDAY);

        val id = 1;
        val film = new Film();
        film.setId(id);
        film.setName(FILM_NAME);
        film.setDescription(FILM_DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        film.setMpa(MPA);

        underTest.save(film, user);
        var savedPopular = underTest.popularFilms(filmRepository.findAll(), 1);
        assertEquals(savedPopular.size(), 1);
        val savedFilmFromList = savedPopular.get(0);
        assertEquals(savedFilmFromList.getId(), id);
        assertEquals(savedFilmFromList.getName(), film.getName());
        assertEquals(savedFilmFromList.getDescription(), film.getDescription());
        assertEquals(savedFilmFromList.getReleaseDate(), film.getReleaseDate());
        assertEquals(savedFilmFromList.getDuration(), film.getDuration());

    }

    @DisplayName("Лайк удаляется")
    @Test
    public void shouldDeleteLike() {

        val user = new User();
        user.setId(1);
        user.setName(NAME);
        user.setLogin(LOGIN);
        user.setEmail(EMAIL);
        user.setBirthday(BIRTHDAY);

        val id = 1;
        val film = new Film();
        film.setId(id);
        film.setName(FILM_NAME);
        film.setDescription(FILM_DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        film.setMpa(MPA);
        underTest.save(film, user);

        underTest.delete(film, user);

    }

}