package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.Collection;
import java.util.List;

public interface UserConverter {
    User findUser(Long id);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    Collection<User> getFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long otherId);
}
