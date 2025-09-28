package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

@Component
public interface UserStorage {
    Collection<User> getUser();

    User newUser(User user);

    User createUser(User newData);

    Optional<User> findById(Long id);
}
