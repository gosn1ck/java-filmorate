package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmsService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Get all films");
        return filmsService.getFilms();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Film> registerFilm(@Valid @RequestBody Film film, Errors errors) {
        log.info("New film registration {}", film);
        if (errors.hasErrors()) {
            log.info("Error during new film registration: {}", errors.getAllErrors());
            return ResponseEntity.internalServerError().body(film);
        }

        return ResponseEntity.ok(filmsService.addFilm(film));
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film, Errors errors) {
        log.info("Update film {}", film);
        if (errors.hasErrors()) {
            log.info("Error during update film: {}", errors.getAllErrors());
            return ResponseEntity.internalServerError().body(film);
        }

        Optional<Film> optFilm = filmsService.updateFilm(film);
        return optFilm.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.internalServerError().body(film));

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Film> getUser(@PathVariable("id") Integer id) {
        log.info("Get film by id: {}", id);
        Optional<Film> optFilm = filmsService.getFilm(id);
        return optFilm.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable("id") Integer filmId,
                          @PathVariable("userId") Integer userId) {

        log.info("Add like to film id: {}, user id: {}", filmId, userId);
        filmsService.addLike(filmId, userId);

    }

    @DeleteMapping(path = "/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable("id") Integer filmId,
                        @PathVariable("userId") Integer userId) {

        log.info("Remove like from id: {}, user id: {}", filmId, userId);
        filmsService.removeLike(filmId, userId);

    }

    @GetMapping(path = "/popular")
    public List<Film> popularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Get popular films with count {}", count);
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be greater than zero");
        }
        return filmsService.popularFilms(count);
    }

}
