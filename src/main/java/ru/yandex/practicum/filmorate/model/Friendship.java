package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Friendship {

    @NotNull(message = "user id should not be null")
    private Integer userId;

    @NotNull(message = "friend id should not be null")
    private Integer friendId;

    @NotNull(message = "confirmed should not be null")
    private Boolean isConfirmed;

    public Friendship() {
    }

    public Friendship(Integer userId, Integer friendId, Boolean isConfirmed) {
        this.userId = userId;
        this.friendId = friendId;
        this.isConfirmed = isConfirmed;
    }
}
