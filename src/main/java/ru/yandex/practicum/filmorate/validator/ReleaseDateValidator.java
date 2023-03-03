package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    private static final LocalDate CINEMA_DATE_OF_BIRTH = LocalDate.of(1895, 12, 28);
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext cxt) {
        return value.isAfter(CINEMA_DATE_OF_BIRTH);
    }
}
