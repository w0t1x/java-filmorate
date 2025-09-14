package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilm() {
        log.debug("Запрос на получение всех фильмов. Количество: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film newFilm(@RequestBody Film film) {
        log.info("Попытка создать новый фильм: {}", film.getName());
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Некорректное название фильма: {}", film.getName());
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.warn("Некорректное количество символов в описании: {}", film.getDescription());
            throw new ValidationException("Описание не может превысить 200 символов");
        }
        if (LocalDate.of(1895, 12, 28).isAfter(film.getReleaseDate())) {
            log.warn("Некорректная дата: {}", film.getReleaseDate());
            throw new ValidationException("Релиз фильма не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null || film.getDuration() < 1) {
            log.warn("Некорректное количество продолжительности фильма: {}", film.getDuration());
            throw new ValidationException("продолжительность фильма не может быть отрицательным числом или нулевым.");
        }

        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан: id={}, name={}, description={}, releaseDate={}, duration={}.", film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    @PutMapping
    public Film createFilm(@RequestBody Film newFilm) {
        log.info("Попытка обновления фильма с id={}", newFilm.getId());
        if (newFilm.getId() == null) {
            log.error("Получен запрос на обновление без id={}", newFilm);
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (newFilm.getDescription() != null) {
                if (newFilm.getDescription().length() > 200) {
                    log.warn("Некорректное количество символов в описании: {}", newFilm.getDescription());
                    throw new ValidationException("Описание не может превышать 200 символов");
                }
                oldFilm.setDescription(newFilm.getDescription());
            }

            if (newFilm.getReleaseDate() != null) {
                if (LocalDate.of(1895, 12, 28).isAfter(newFilm.getReleaseDate())) {
                    log.warn("Некорректная дата: {}", newFilm.getReleaseDate());
                    throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
                }
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }

            if (newFilm.getDuration() != null) {
                if (newFilm.getDuration() < 1) {
                    log.warn("Некорректное количество продолжительности фильма: {}", newFilm.getDuration());
                    throw new ValidationException("Продолжительность фильма должна быть положительным числом или нулевым.");
                }
                oldFilm.setDuration(newFilm.getDuration());
            }

            if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
                boolean existsName = films.values().stream()
                        .anyMatch(film -> !film.getId().equals(newFilm.getId()) && film.getName().equals(newFilm.getName()));
                if (existsName) {
                    log.warn("Некорректный выбор названия фильма: name={}", newFilm.getName());
                    throw new ValidationException("Фильм с таким названием уже существует");
                }
                oldFilm.setName(newFilm.getName());
            }

            films.put(oldFilm.getId(), oldFilm);
            log.info("Фильм успешно обновлён: id={}, name={}, description={}, releaseDate={}, duration={}.", newFilm.getId(), newFilm.getName(), newFilm.getDescription(), newFilm.getReleaseDate(), newFilm.getDuration());
            return oldFilm;
        }
        log.warn("Попытка обновить несуществующего фильма с id={}", newFilm.getId());
        throw new ValidationException("Пост с id = " + newFilm.getId() + " не найден");
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