package com.example.artspace.services;

import com.example.artspace.repositories.UserRepository;
import org.springframework.stereotype.Service;
import com.example.artspace.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User create(User user) {
        User existing = findByEmail(user.getEmail());
        if (existing != null) {
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        return userRepository.save(user);
    }

    public User update(Long id, User input) {
        User existing = findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(input.getName());
        existing.setEmail(input.getEmail());

        return userRepository.save(existing);
    }

    public boolean delete(Long id) {
        User existing = findById(id);

        if (existing == null) {
            return false;
        }
        userRepository.delete(existing);
        return true;
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
