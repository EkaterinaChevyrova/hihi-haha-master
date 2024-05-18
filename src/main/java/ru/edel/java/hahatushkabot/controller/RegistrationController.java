package ru.edel.java.hahatushkabot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edel.java.hahatushkabot.model.User;
import ru.edel.java.hahatushkabot.model.UserAuthority;
import ru.edel.java.hahatushkabot.server.UserService;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> registration(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        userService.registration(username, password);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}/roles")
    public ResponseEntity<Void> updateUserRoles(@PathVariable Long userId, @RequestBody List<UserAuthority> authorities) {
        userService.updateUserRoles(userId, authorities);
        return ResponseEntity.ok().build();
    }

}
