package ru.yandex.practicum.filmorate.repository.impl;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({DbFilmRepository.class, DbMpaRepository.class, DbGenreRepository.class})
class DbFilmRepositoryTest {

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

    @Autowired
    @Qualifier("Db")
    private FilmRepository underTest;
    @Autowired
    @Qualifier("Db")
    private MpaRepository mpaRepository;
    @Autowired
    @Qualifier("Db")
    private GenreRepository genreRepository;

    @DisplayName("Фильм сохраняется")
    @Test
    public void shouldSaveFilm() {

        val shouldBeEmpty = underTest.findAll();
        assertEquals(shouldBeEmpty.size(), 0);

        val id = 1;
        val film = new Film();
        film.setId(id);
        film.setName(FILM_NAME);
        film.setDescription(FILM_DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        film.setMpa(MPA);

        underTest.save(film);

        val savedOptFilm = underTest.findById(film.getId());
        assertTrue(savedOptFilm.isPresent());
        val savedFilm = savedOptFilm.get();
        assertEquals(savedFilm.getId(), id);
        assertEquals(savedFilm.getName(), film.getName());
        assertEquals(savedFilm.getDescription(), film.getDescription());
        assertEquals(savedFilm.getReleaseDate(), film.getReleaseDate());
        assertEquals(savedFilm.getDuration(), film.getDuration());

        val savedFilms = underTest.findAll();
        assertEquals(savedFilms.size(), 1);
        val savedFilmFromList = savedFilms.get(0);
        assertEquals(savedFilmFromList.getId(), id);
        assertEquals(savedFilmFromList.getName(), film.getName());
        assertEquals(savedFilmFromList.getDescription(), film.getDescription());
        assertEquals(savedFilmFromList.getReleaseDate(), film.getReleaseDate());
        assertEquals(savedFilmFromList.getDuration(), film.getDuration());

    }

    @DisplayName("Фильм сохраняется c жанрами")
    @Test
    public void shouldSaveFilmWithGenres() {

        val id = 1;
        val film = new Film();
        film.setId(id);
        film.setName(FILM_NAME);
        film.setDescription(FILM_DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        film.setMpa(MPA);
        val genres = new HashSet<>(
                List.of(
                        new Genre().builder()
                                .name("Комедия")
                                .id(1)
                                .build(),
                        new Genre().builder()
                                .name("Драма")
                                .id(2)
                                .build()
                ));
        film.setGenres(genres);

        underTest.save(film);

        val savedOptFilm = underTest.findById(film.getId());
        assertTrue(savedOptFilm.isPresent());
        val savedFilm = savedOptFilm.get();
        assertEquals(savedFilm.getId(), id);
        assertEquals(savedFilm.getGenres(), film.getGenres());

        val savedFilms = underTest.findAll();
        assertEquals(savedFilms.size(), 1);
        val savedFilmFromList = savedFilms.get(0);
        assertEquals(savedFilmFromList.getId(), id);
        assertEquals(savedFilmFromList.getGenres(), film.getGenres());

    }

    @DisplayName("Не возвращается фильм по не существующему идентификатору 9999")
    @Test
    public void shouldNotFindFilmById() {
        val optFilm = underTest.findById(9999);
        assertFalse(optFilm.isPresent());
    }

    @DisplayName("Фильм обновляется")
    @Test
    public void shouldUpdateFilm() {

        val id = 1;
        val film = new Film();
        film.setId(id);
        film.setName(FILM_NAME);
        film.setDescription(FILM_DESCRIPTION);
        film.setReleaseDate(RELEASE_DATE);
        film.setDuration(DURATION);
        film.setMpa(MPA);

        underTest.save(film);

        val updatedName = "Film Updated";
        val updatedDesc = "New film update description";
        val updatedDuration = 190;
        val updatedReleaseDate = LocalDate.of(1989, 4, 17);
        val updatedMpa = new Mpa(2, "PG");
        film.setName(updatedName);
        film.setDescription(updatedDesc);
        film.setDuration(updatedDuration);
        film.setReleaseDate(updatedReleaseDate);
        film.setMpa(updatedMpa);

        underTest.update(film);
        val savedOptFilm = underTest.findById(id);
        assertTrue(savedOptFilm.isPresent());
        val savedFilm = savedOptFilm.get();
        assertEquals(savedFilm.getId(), id);
        assertEquals(savedFilm.getName(), updatedName);
        assertEquals(savedFilm.getDescription(), updatedDesc);
        assertEquals(savedFilm.getReleaseDate(), updatedReleaseDate);
        assertEquals(savedFilm.getDuration(), updatedDuration);
        assertEquals(savedFilm.getMpa(), updatedMpa);

        val savedFilms = underTest.findAll();
        assertEquals(savedFilms.size(), 1);
        val savedFilmFromList = savedFilms.get(0);
        assertEquals(savedFilmFromList.getId(), id);
        assertEquals(savedFilmFromList.getName(), updatedName);
        assertEquals(savedFilmFromList.getDescription(), updatedDesc);
        assertEquals(savedFilmFromList.getReleaseDate(), updatedReleaseDate);
        assertEquals(savedFilmFromList.getDuration(), updatedDuration);
        assertEquals(savedFilmFromList.getMpa(), updatedMpa);

    }

}