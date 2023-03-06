package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;

    @Email(message = "Email is not valid")
    private String email;

    @NotNull(message = "Login should not be null")
    @NotEmpty(message = "Login should not be empty")
    @Pattern(regexp = "\\S+", message = "Login should not contains space")
    private String login;
    private String name;

    @Past(message = "Birthday should be in the past")
    private LocalDate birthday;
}
