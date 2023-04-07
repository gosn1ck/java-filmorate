package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    public List<Mpa> getMpa() {
        log.info("Get all mpa");
        return mpaService.getMpa();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Mpa> getMpaById(@PathVariable("id") Integer id) {
        log.info("Get mpa by id: {}", id);
        Optional<Mpa> optMpa = mpaService.getMpaById(id);
        return optMpa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
