package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.UniqueFilmNameValidator;

import jakarta.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    public FilmController() {
        UniqueFilmNameValidator.setFilms(films);
    }

    @GetMapping
    public Collection<Film> getFilm() {
        log.debug("Запрос на получение всех фильмов. Количество: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film newFilm(@Valid @RequestBody Film film) {
        log.info("Попытка создать новый фильм: {}", film.getName());

        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан: id={}, name={}, description={}, releaseDate={}, duration={}.",
                film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    @PutMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        log.info("Попытка обновления фильма с id={}", newFilm.getId());

        if (!films.containsKey(newFilm.getId())) {
            log.warn("Попытка обновить несуществующего фильма с id={}", newFilm.getId());
            throw new ValidationException("Фильм с id = " + newFilm.getId() + " не найден");
        }

        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());

        films.put(oldFilm.getId(), oldFilm);
        log.info("Фильм успешно обновлён: id={}, name={}, description={}, releaseDate={}, duration={}.",
                newFilm.getId(), newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration());
        return oldFilm;
    }

    private long generateId() {
        long currentMax = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMax;
    }
}