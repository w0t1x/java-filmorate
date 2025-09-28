package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService implements FilmConverter {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film findFilm(Long filmId) {
        return filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + filmId + " не найден"));
    }

    public void addLike(Long filmId, Long userId) {
        Film film = findFilm(filmId);

        // Проверяем существование пользователя
        if (!userStorage.findById(userId).isPresent()) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }

        film.getLikes().add(userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = findFilm(filmId);

        // Проверяем существование пользователя
        if (!userStorage.findById(userId).isPresent()) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }

        // Удаляем лайк без лишних проверок
        film.getLikes().remove(userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getFilm().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
