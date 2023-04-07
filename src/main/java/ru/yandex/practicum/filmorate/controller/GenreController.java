package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Get all genres");
        return genreService.getGenres();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable("id") Integer id) {
        log.info("Get genre by id: {}", id);
        Optional<Genre> optGenre = genreService.getGenreById(id);
        return optGenre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
