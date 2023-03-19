package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
public class ApiException {
    private final String message;
    private final HttpStatus status;
    private final ZonedDateTime dateTime;
}
