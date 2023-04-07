package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {

    private final MpaRepository mpaRepository;

    public MpaService(@Qualifier("Db") MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    public List<Mpa> getMpa() {
        return mpaRepository.findAll();
    }

    public Optional<Mpa> getMpaById(Integer id) {
        return mpaRepository.findById(id);
    }

}
