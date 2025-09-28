package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserConverter {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User findUser(Long id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
    }

    public void addFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> getFriends(Long userId) {
        return findUser(userId).getFriends().stream()
                .map(this::findUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> common = new HashSet<>(findUser(userId).getFriends());
        common.retainAll(findUser(otherId).getFriends());
        return common.stream().map(this::findUser)
                .collect(Collectors.toList());
    }
}
