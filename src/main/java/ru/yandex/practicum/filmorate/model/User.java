package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;

    @Email
    @NonNull
    private String email;

    @NonNull
    @NotEmpty
    @Pattern(regexp="\\S+")
    private String login;
    private String name;

    @Past
    private LocalDate birthday;
}
