package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getUser() {
        log.debug("Запрос на получение всех пользователей. Количество: {}", users.size());
        return users.values();
    }

    public User newUser(User user) { // <-- @Valid
        log.info("Попытка создать нового пользователя: email={}, login={}", user.getEmail(), user.getLogin());

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Имя пользователя не установлено. Используем по умолчанию: {}", user.getLogin());
        }

        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан: id={}, email={}, login={}", user.getId(), user.getEmail(), user.getLogin());
        return user;
    }

    public User createUser(User newData) { // <-- @Valid
        log.info("Попытка обновления пользователя с id={}", newData.getId());

        if (!users.containsKey(newData.getId())) {
            log.warn("Попытка обновить несуществующего пользователя с id={}", newData.getId());
            throw new NotFoundException("Пользователь с id = " + newData.getId() + " не найден");
        }

        User oldUser = users.get(newData.getId());

        oldUser.setEmail(newData.getEmail());
        oldUser.setLogin(newData.getLogin());
        oldUser.setBirthday(newData.getBirthday());

        if (newData.getName() == null || newData.getName().isBlank()) {
            oldUser.setName(newData.getLogin());
            log.debug("Имя не указано. Установлено по логину: {}", oldUser.getName());
        } else {
            oldUser.setName(newData.getName());
            log.debug("Установлено новое имя: {}", oldUser.getName());
        }

        users.put(oldUser.getId(), oldUser);
        log.info("Пользователь успешно обновлён: id={}, email={}, login={}", oldUser.getId(), oldUser.getEmail(), oldUser.getLogin());
        return oldUser;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
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
