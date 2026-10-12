package com.example.artspace.services;

import com.example.artspace.models.User;
import com.example.artspace.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_shouldReturnAllUsers() {
        User user = new User();
        user.setName("Alex");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("Alex", result.get(0).getName());
        verify(userRepository).findAll();
    }

    @Test
    void findById_shouldReturnUserWhenExists() {
        User user = new User();
        user.setName("Alex");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertNotNull(result);
        assertEquals("Alex", result.getName());
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        User result = userService.findById(99L);

        assertNull(result);
    }

    @Test
    void findByEmail_shouldReturnUserWhenExists() {
        User user = new User();
        user.setEmail("alex@example.com");

        when(userRepository.findByEmail("alex@example.com"))
                .thenReturn(Optional.of(user));

        User result = userService.findByEmail("alex@example.com");

        assertNotNull(result);
        assertEquals("alex@example.com", result.getEmail());
    }

    @Test
    void findByEmail_shouldReturnNullWhenNotFound() {
        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        User result = userService.findByEmail("missing@example.com");

        assertNull(result);
    }

    @Test
    void create_shouldEncodePasswordAndSetUserRole() {
        User user = new User();
        user.setName("Alex");
        user.setEmail("alex@example.com");
        user.setPassword("password123");

        when(userRepository.findByEmail("alex@example.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("USER", result.getRole());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }

    @Test
    void create_shouldReturnNullWhenEmailAlreadyExists() {
        User existing = new User();
        existing.setEmail("alex@example.com");

        User input = new User();
        input.setEmail("alex@example.com");
        input.setPassword("password123");

        when(userRepository.findByEmail("alex@example.com"))
                .thenReturn(Optional.of(existing));

        User result = userService.create(input);

        assertNull(result);
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void update_shouldUpdateExistingUser() {
        User existing = new User();
        existing.setName("Alex");
        existing.setEmail("old@example.com");
        existing.setPassword("encodedPassword");

        User input = new User();
        input.setName("Alex Updated");
        input.setEmail("new@example.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(userRepository.save(existing)).thenReturn(existing);

        User result = userService.update(1L, input);

        assertNotNull(result);
        assertEquals("Alex Updated", result.getName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository).save(existing);
    }

    @Test
    void update_shouldReturnNullWhenUserNotFound() {
        User input = new User();
        input.setName("Updated");

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        User result = userService.update(99L, input);

        assertNull(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteExistingUser() {
        User user = new User();
        user.setName("Alex");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        boolean result = userService.delete(1L);

        assertTrue(result);
        verify(userRepository).delete(user);
    }

    @Test
    void delete_shouldReturnFalseWhenNotFound() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        boolean result = userService.delete(99L);

        assertFalse(result);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void passwordMatches_shouldReturnTrueWhenCorrect() {
        when(passwordEncoder.matches("password123", "encodedPassword"))
                .thenReturn(true);

        boolean result = userService.passwordMatches(
                "password123", "encodedPassword");

        assertTrue(result);
    }

    @Test
    void passwordMatches_shouldReturnFalseWhenIncorrect() {
        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        boolean result = userService.passwordMatches(
                "wrongPassword", "encodedPassword");

        assertFalse(result);
    }
}
