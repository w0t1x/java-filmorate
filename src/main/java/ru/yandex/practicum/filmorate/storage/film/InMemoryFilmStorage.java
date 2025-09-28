package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getFilm() {
        log.debug("Запрос на получение всех фильмов. Количество: {}", films.size());
        return films.values();
    }

    public Film newFilm(Film film) {
        log.info("Попытка создать новый фильм: {}", film.getName());

        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан: id={}, name={}, description={}, releaseDate={}, duration={}.",
                film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        return film;
    }

    public Film createFilm(Film newFilm) {
        log.info("Попытка обновления фильма с id={}", newFilm.getId());

        if (!films.containsKey(newFilm.getId())) {
            log.warn("Попытка обновить несуществующего фильма с id={}", newFilm.getId());
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
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

    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(films.get(id));
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
