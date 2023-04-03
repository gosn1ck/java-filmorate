package ru.yandex.practicum.filmorate.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("InMemory")
public class InMemoryMpaRepository implements MpaRepository {

    private final HashMap<Integer, Mpa> mpas = new HashMap<>();

    @Override
    public List<Mpa> findAll() {
        return new ArrayList<>(mpas.values());
    }

    @Override
    public Mpa save(Mpa mpa) {
        mpas.put(mpa.getId(), mpa);
        return mpa;
    }

    @Override
    public Optional<Mpa> findById(Integer id) {
        if (mpas.containsKey(id)) {
            return Optional.of(mpas.get(id));
        }

        return Optional.empty();
    }

}
