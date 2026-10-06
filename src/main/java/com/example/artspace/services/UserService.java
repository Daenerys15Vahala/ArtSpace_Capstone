package com.example.artspace.services;

import com.example.artspace.repositories.UserRepository;
import org.springframework.stereotype.Service;
import com.example.artspace.models.User;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
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
}
