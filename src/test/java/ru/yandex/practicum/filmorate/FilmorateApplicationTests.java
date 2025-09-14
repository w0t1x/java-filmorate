package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmorateApplicationTests {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void shouldCreateValidFilm() {
        Film film = new Film();
        film.setName("Interstellar");
        film.setDescription("Great sci-fi movie");
        film.setReleaseDate(LocalDate.of(2014, 11, 7));
        film.setDuration(169L);

        Film created = filmController.newFilm(film); // исправлено!

        assertNotNull(created.getId());
        assertEquals("Interstellar", created.getName());
    }

    @Test
    void shouldReturnAllFilms() {
        Film film = new Film();
        film.setName("Matrix");
        film.setDescription("Classic sci-fi");
        film.setReleaseDate(LocalDate.of(1999, 3, 31));
        film.setDuration(136L);

        filmController.newFilm(film); // исправлено!
        Collection<Film> films = filmController.getFilm();

        assertEquals(1, films.size());
    }
}
