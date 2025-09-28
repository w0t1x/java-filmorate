package ru.yandex.practicum.filmorate.inteface;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.validator.UniqueFilmNameValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueFilmNameValidator.class)
@Documented
public @interface UniqueFilmName {
    String message() default "Фильм с таким названием уже существует";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}