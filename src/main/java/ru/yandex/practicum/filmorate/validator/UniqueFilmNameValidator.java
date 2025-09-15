package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.inteface.UniqueFilmName;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UniqueFilmNameValidator implements ConstraintValidator<UniqueFilmName, Film> {

    private static final Map<Long, Film> films = new ConcurrentHashMap<>();

    public static void setFilms(Map<Long, Film> filmStorage) {
        films.clear();
        if (filmStorage != null) {
            films.putAll(filmStorage);
        }
    }

    @Override
    public boolean isValid(Film film, ConstraintValidatorContext context) {
        if (film == null || film.getName() == null) {
            return true;
        }

        return films.values().stream()
                .noneMatch(existingFilm ->
                        !existingFilm.getId().equals(film.getId()) &&
                                existingFilm.getName().equals(film.getName()));
    }
}