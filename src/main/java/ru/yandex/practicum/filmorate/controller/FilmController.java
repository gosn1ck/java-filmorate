package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmsService;

    @Autowired
    public FilmController(FilmService filmsService) {
        this.filmsService = filmsService;
    }

    @GetMapping
    public List<Film> getFilms(){
        return filmsService.getFilms();
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Film> registerFilm(@Valid @RequestBody Film film, Errors errors) {
        log.info("New film registration {}", film);
        if (errors.hasErrors()) {
            return ResponseEntity.internalServerError().body(film);
        }

        return ResponseEntity.ok(filmsService.addFilm(film));
    }

    @PutMapping(consumes="application/json")
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film, Errors errors) {
        log.info("Update film {}", film);
        if (errors.hasErrors()) {
            return ResponseEntity.internalServerError().body(film);
        }

        Optional<Film> optFilm = filmsService.updateFilm(film);
        if (!optFilm.isPresent()) {
            return ResponseEntity.internalServerError().body(film);
        }

        return ResponseEntity.ok(optFilm.get());
    }
}
