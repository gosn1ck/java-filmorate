package ru.yandex.practicum.filmorate.repository.impl;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(DbGenreRepository.class)
class DbGenreRepositoryTest {

    @Autowired
    private DbGenreRepository underTest;

    @DisplayName("Возвращается 6 предзаполненных жанров")
    @Test
    public void shouldFindAllGenres() {
        assertEquals(underTest.findAll().size(), 6);
    }

    @DisplayName("Возвращается жанр по идентификатору 1")
    @Test
    public void shouldFindGenreById() {
        val optGenre = underTest.findById(1);
        assertTrue(optGenre.isPresent());
        val genre = optGenre.get();
        assertEquals(genre.getId(), 1);
        assertEquals(genre.getName(), "Комедия");
    }

    @DisplayName("Не возвращается жанр по не существующему идентификатору 9999")
    @Test
    public void shouldNotFindGenreById() {
        val optGenre = underTest.findById(9999);
        assertFalse(optGenre.isPresent());
    }

    @DisplayName("Жанр сохраняется")
    @Test
    public void shouldSaveGenre() {
        val id = 555;
        val genre = new Genre(id, "Сериал");

        underTest.save(genre);

        val optGenre = underTest.findById(id);
        assertTrue(optGenre.isPresent());
        val savedGenre = optGenre.get();
        assertEquals(savedGenre.getId(), id);
        assertEquals(savedGenre.getName(), "Сериал");
    }

}