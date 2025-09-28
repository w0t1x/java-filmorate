package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getFilm();

    Film newFilm(Film film);

    Film createFilm(Film newFilm);

    Optional<Film> findById(Long id);
}
