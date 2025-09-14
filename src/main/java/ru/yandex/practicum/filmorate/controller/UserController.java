package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUser() {
        log.debug("Запрос на получение всех пользователей. Количество: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User newUser(@RequestBody User user) {
        log.info("Попытка создать нового пользователя: email={}, login={}", user.getEmail(), user.getLogin());
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.warn("Некорректный email при создании пользователя: {}", user.getEmail());
            throw new ValidationException("Почта не может быть пустой и должна содержать символ '@'");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Некорректный login при создании пользователя: {}", user.getLogin());
            log.error(String.valueOf(new ValidationException("Логин не может быть пустым и содержать пробелы")));
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя не установленно. Используем по умолчанию: {}", user.getLogin());
        } else {
            user.setName(user.getName());
            log.debug("Имя пользователя установленно: {}", user.getName());
        }
        if (user.getBirthday() == null) {
            log.warn("Не установлена дата рождения пользователя: email={}", user.getEmail());
            throw new ValidationException("Дата рождения должна быть.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Неверно установлена дата рождения пользователя: email={}", user.getEmail());
            throw new ValidationException("дата рождения не может быть в будущем.");
        }

        user.setId(generateId());
        users.put(user.getId(), user);

        log.info("Пользователь успешно создан: id={}, email={}, login={}", user.getId(), user.getEmail(), user.getLogin());
        return user;
    }

    @PutMapping
    public User createUser(@RequestBody User newData) {
        log.info("Попытка обновления пользователя с id={}", newData.getId());

        if (newData.getId() == null) {
            log.error("Получен запрос на обновление без id: {}", newData);
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newData.getId())) {
            User oldUser = users.get(newData.getId());
            if (newData.getEmail() != null) {
                if (!newData.getEmail().contains("@")) {
                    log.warn("Некорректный email при обновлении: {}", newData.getEmail());
                    throw new ValidationException("Почта не может быть пустой и должна содержать символ '@'");
                }
                oldUser.setEmail(newData.getEmail());
            }
            if (newData.getLogin() != null) {
                if (newData.getLogin().isBlank()) {
                    log.warn("Некорректный логин при обновлении: {}", newData.getLogin());
                    throw new ValidationException("Логин не может быть пустым и содержать пробелы");
                }
                oldUser.setLogin(newData.getLogin());
                log.debug("Email обновлён: {}", oldUser.getEmail());
            }
            if (newData.getName() == null) {
                if (newData.getLogin() == null) {
                    oldUser.setName(oldUser.getName());
                    log.debug("Имя и логин не указаны. Сохранено без изменений {}", oldUser.getName());
                }
                oldUser.setName(newData.getLogin());
                log.debug("Имя не указано. Установлено по логину: {}", oldUser.getName());
            } else {
                oldUser.setName(newData.getName());
                log.debug("Установлено по новому имени: {}", oldUser.getName());
            }
            if (newData.getBirthday() != null) {
                if (newData.getBirthday().isBefore(LocalDate.now())) {
                    oldUser.setBirthday(newData.getBirthday());
                    log.debug("Установленно новая дата рождения: {}", oldUser.getBirthday());
                }
            }

            users.put(oldUser.getId(), oldUser);

            log.info("Пользователь успешно обновлён: id={}, email={}, login={}", oldUser.getId(), oldUser.getEmail(), oldUser.getLogin());

            return oldUser;
        }
        log.warn("Попытка обновить несуществующего пользователя с id={}", newData.getId());
        throw new ValidationException("Пост с id = " + newData.getId() + " не найден");
    }

    private long generateId() {
        long currentMax = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.trace("Генерация нового ID: предыдущий максимум = {}, новый ID = {}", currentMax, currentMax + 1);
        return ++currentMax;
    }
}
