package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.info("Get all users");
        return userService.getUsers();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user, Errors errors) {
        log.info("New user registration {}", user);
        if (errors.hasErrors()) {
            log.info("Error during new user registration: {}", errors.getAllErrors());
            return ResponseEntity.internalServerError().body(user);
        }
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user, Errors errors) {
        log.info("Update user {}", user);
        if (errors.hasErrors()) {
            log.info("Error during update user: {}", errors.getAllErrors());
            return ResponseEntity.internalServerError().body(user);
        }

        Optional<User> optUser = userService.updateUser(user);
        return optUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.internalServerError().body(user));

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Integer userId) {
        log.info("Get user id: {}", userId);
        Optional<User> optUser = userService.getUser(userId);
        return optUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/{id}/friends")
    public List<User> friends(@PathVariable("id") Integer userId) {
        log.info("User's friends with id: {}", userId);
        return userService.friends(userId);
    }

    @PutMapping(path = "/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId,
                          @PathVariable("friendId") Integer friendId) {

        log.info("Add to id: {}, friend: {}", userId, friendId);
        if (userId.equals(friendId)) {
            throw new InternalServerException("impossible to add friend with same id");
        }

        userService.addFriend(userId, friendId);

    }

    @DeleteMapping(path = "/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Integer userId,
                             @PathVariable("friendId") Integer friendId) {

        log.info("Remove from id: {}, friend: {}", userId, friendId);
        if (userId.equals(friendId)) {
            throw new InternalServerException("impossible to remove friend with same id");
        }

        userService.removeFriend(userId, friendId);

    }

    @GetMapping(path = "{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer userId,
                                       @PathVariable("otherId") Integer otherId) {
        log.info("Get common friends");
        return userService.getCommonFriends(userId, otherId);
    }

}
