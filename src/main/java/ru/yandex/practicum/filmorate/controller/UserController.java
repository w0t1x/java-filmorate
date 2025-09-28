package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;

    }

    @GetMapping
    public Collection<User> getUser() {
        return userStorage.getUser();
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Long userId,
                                       @PathVariable Long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    public User newUser(@Valid @RequestBody User user) { // <-- @Valid
        return userStorage.newUser(user);
    }

    @PutMapping
    public User createUser(@Valid @RequestBody User newData) { // <-- @Valid
        return userStorage.createUser(newData);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long userId,
                          @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Long userId,
                             @PathVariable Long friendId) {
        userService.deleteFriend(userId, friendId);
    }
}