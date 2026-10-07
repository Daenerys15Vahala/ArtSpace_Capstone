package com.example.artspace.controllers;


import com.example.artspace.repositories.UserRepository;
import com.example.artspace.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.artspace.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User input) {
        User saved = userService.create(input);
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User input) {
        User user = userService.findByEmail(input.getEmail());
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
        if (!userService.passwordMatches(input.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
        return ResponseEntity.ok(user);
    }

}
