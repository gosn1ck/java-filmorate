package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {

    @NotNull(message = "user id should not be null")
    private Integer userId;

    @NotNull(message = "friend id should not be null")
    private Integer friendId;

    @NotNull(message = "confirmed should not be null")
    private Boolean isConfirmed;

}
