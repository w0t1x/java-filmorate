package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.inteface.ReleaseDate;
import ru.yandex.practicum.filmorate.inteface.UniqueFilmName;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@UniqueFilmName
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза должна быть указана")
    @PastOrPresent(message = "Дата релиза не может быть в будущем")
    @ReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private Long duration;

    private Set<Long> likes = new HashSet<>();
}