package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> registerUser(@Valid  @RequestBody User user, Errors errors) {
        log.info("Update user {}", user);
        if (errors.hasErrors()) {
            return ResponseEntity.internalServerError().body(user);
        }
        return ResponseEntity.ok(userService.addUser(user));
    }

    @PutMapping(consumes="application/json")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.internalServerError().body(user);
        }

        Optional<User> optUser = userService.updateUser(user);
        if (!optUser.isPresent()) {
            return ResponseEntity.internalServerError().body(user);
        }

        return ResponseEntity.ok(optUser.get());
    }
}
