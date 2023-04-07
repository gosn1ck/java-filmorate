package ru.yandex.practicum.filmorate.repository.impl;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(DbMpaRepository.class)
class DbMpaRepositoryTest {

    @Autowired
    private DbMpaRepository underTest;

    @DisplayName("Возвращается 5 предзаполненных рейтинга")
    @Test
    public void shouldFindAllMpa() {
        assertEquals(underTest.findAll().size(), 5);
    }

    @DisplayName("Возвращается рейтинг по идентификатору 1")
    @Test
    public void shouldFindMpaById() {
        val optMpa = underTest.findById(1);
        assertTrue(optMpa.isPresent());
        val mpa = optMpa.get();
        assertEquals(mpa.getId(), 1);
        assertEquals(mpa.getName(), "G");
    }

    @DisplayName("Не возвращается рейтинг по не существующему идентификатору 9999")
    @Test
    public void shouldNotFindMpaById() {
        val optMpa = underTest.findById(9999);
        assertFalse(optMpa.isPresent());
    }

    @DisplayName("Рейтинг сохраняется")
    @Test
    public void shouldSaveMpa() {
        val id = 555;
        val mpa = new Mpa(id, "MP-27");

        underTest.save(mpa);

        val optMpa = underTest.findById(id);
        assertTrue(optMpa.isPresent());
        val saveMpa = optMpa.get();
        assertEquals(saveMpa.getId(), id);
        assertEquals(saveMpa.getName(), "MP-27");
    }

}