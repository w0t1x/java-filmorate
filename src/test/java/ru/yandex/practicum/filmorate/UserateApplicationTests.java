package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserateApplicationTests {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void shouldCreateValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User created = userController.newUser(user); // исправлено ✅

        assertNotNull(created.getId());
        assertEquals("test@example.com", created.getEmail());
    }

    @Test
    void shouldSetNameToLoginIfNameIsEmpty() {
        User user = new User();
        user.setEmail("noname@example.com");
        user.setLogin("loginOnly");
        user.setName(""); // пустое имя
        user.setBirthday(LocalDate.of(1985, 1, 1));

        User created = userController.newUser(user); // исправлено ✅

        assertEquals("loginOnly", created.getName());
    }

    @Test
    void shouldReturnAllUsers() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setLogin("john123");
        user.setName("John");
        user.setBirthday(LocalDate.of(1995, 5, 5));

        userController.newUser(user); // исправлено ✅
        Collection<User> users = userController.getUser();

        assertEquals(1, users.size());
    }
}
