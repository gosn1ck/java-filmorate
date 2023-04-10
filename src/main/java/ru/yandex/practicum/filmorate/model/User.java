package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    private Integer id;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotNull(message = "Login should not be null")
    @NotEmpty(message = "Login should not be empty")
    @Pattern(regexp = "\\S+", message = "Login should not contains space")
    private String login;
    private String name;

    @Past(message = "Birthday should be in the past")
    private LocalDate birthday;

    public User(Integer id, String name, String login, String email, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }
}
