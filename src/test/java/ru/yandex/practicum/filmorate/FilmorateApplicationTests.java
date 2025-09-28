//package ru.yandex.practicum.filmorate;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.controller.FilmController;
//import ru.yandex.practicum.filmorate.controller.UserController;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class FilmorateApplicationTests {
//
//    private FilmController filmController;
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        filmController = new FilmController();
//        userController = new UserController();
//    }
//
//    // ===================== FILM =====================
//    @Test
//    void shouldUpdateExistingFilm() {
//        Film film = new Film();
//        film.setName("Old Name");
//        film.setDescription("Old description");
//        film.setReleaseDate(LocalDate.of(2000, 1, 1));
//        film.setDuration(120L);
//
//        Film created = filmController.newFilm(film);
//
//        Film update = new Film();
//        update.setId(created.getId());
//        update.setName("New Name");
//        update.setDescription("New description");
//        update.setReleaseDate(LocalDate.of(2010, 5, 5));
//        update.setDuration(150L);
//
//        Film updated = filmController.createFilm(update);
//
//        assertEquals("New Name", updated.getName());
//        assertEquals("New description", updated.getDescription());
//        assertEquals(150L, updated.getDuration());
//    }
//
//    @Test
//    void shouldThrowWhenUpdateFilmWithoutId() {
//        Film film = new Film();
//        film.setName("Some film");
//        film.setDescription("desc");
//        film.setReleaseDate(LocalDate.of(2001, 1, 1));
//        film.setDuration(100L);
//
//        filmController.newFilm(film);
//
//        Film update = new Film(); // id не задан
//        update.setName("Another");
//
//        assertThrows(ValidationException.class, () -> filmController.createFilm(update));
//    }
//
//    // ===================== USER =====================
//    @Test
//    void shouldUpdateExistingUser() {
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setLogin("login1");
//        user.setName("Old Name");
//        user.setBirthday(LocalDate.of(1990, 1, 1));
//
//        User created = userController.newUser(user);
//
//        User update = new User();
//        update.setId(created.getId());
//        update.setEmail("new@example.com");
//        update.setLogin("newLogin");
//        update.setName("New Name");
//        update.setBirthday(LocalDate.of(1995, 5, 5));
//
//        User updated = userController.createUser(update);
//
//        assertEquals("new@example.com", updated.getEmail());
//        assertEquals("newLogin", updated.getLogin());
//        assertEquals("New Name", updated.getName());
//        assertEquals(LocalDate.of(1995, 5, 5), updated.getBirthday());
//    }
//
//    @Test
//    void shouldThrowWhenUpdateUserWithoutId() {
//        User user = new User();
//        user.setEmail("valid@example.com");
//        user.setLogin("login123");
//        user.setName("User");
//        user.setBirthday(LocalDate.of(1980, 2, 2));
//
//        userController.newUser(user);
//
//        User update = new User(); // id не задан
//        update.setEmail("invalid@example.com");
//
//        assertThrows(ValidationException.class, () -> userController.createUser(update));
//    }
//}
