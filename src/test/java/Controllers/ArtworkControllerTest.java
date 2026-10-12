
package com.example.artspace.controllers;

import com.example.artspace.models.Artwork;
import com.example.artspace.models.User;
import com.example.artspace.services.ArtworkService;
import com.example.artspace.services.UserService;
import com.example.artspace.services.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtworkControllerTest {

    @Mock
    private ArtworkService artworkService;

    @Mock
    private UserService userService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private Principal principal;

    @InjectMocks
    private ArtworkController artworkController;

    private User createUser(Long id, String role) {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        user.setEmail("artist@example.com");
        return user;
    }

    private Artwork createArtwork(User owner) {
        Artwork artwork = new Artwork();
        artwork.setTitle("Sunset");
        artwork.setUser(owner);
        return artwork;
    }

    @Test
    void findAll_shouldReturnAllArtworks() {
        Artwork artwork = new Artwork();

        when(artworkService.findAll())
                .thenReturn(List.of(artwork));

        List<Artwork> result = artworkController.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findById_shouldReturn200WhenArtworkExists() {
        Artwork artwork = new Artwork();
        artwork.setTitle("Sunset");

        when(artworkService.findById(1L))
                .thenReturn(artwork);

        ResponseEntity<Artwork> response =
                artworkController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Sunset", response.getBody().getTitle());
    }

    @Test
    void findById_shouldReturn404WhenNotFound() {
        when(artworkService.findById(99L))
                .thenReturn(null);

        ResponseEntity<Artwork> response =
                artworkController.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByCategoryId_shouldReturnArtworks() {
        when(artworkService.findByCategoryId(1L))
                .thenReturn(List.of(new Artwork()));

        List<Artwork> result =
                artworkController.findByCategoryId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void findByUserId_shouldReturnArtworks() {
        when(artworkService.findByUserId(1L))
                .thenReturn(List.of(new Artwork()));

        List<Artwork> result =
                artworkController.findByUserId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void create_shouldReturn201WhenSuccessful() {
        User user = createUser(1L, "USER");
        Artwork input = new Artwork();

        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(user);
        when(artworkService.create(input))
                .thenReturn(input);

        ResponseEntity<Artwork> response =
                artworkController.create(input, principal);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(user, input.getUser());
    }

    @Test
    void create_shouldReturn400WhenInvalid() {
        User user = createUser(1L, "USER");
        Artwork input = new Artwork();

        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(user);
        when(artworkService.create(input))
                .thenReturn(null);

        ResponseEntity<Artwork> response =
                artworkController.create(input, principal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void update_shouldReturn404WhenArtworkNotFound() {
        when(artworkService.findById(99L))
                .thenReturn(null);

        ResponseEntity<Artwork> response =
                artworkController.update(99L, new Artwork(), principal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void update_shouldReturn403WhenNotOwnerOrAdmin() {
        User owner = createUser(1L, "USER");
        User otherUser = createUser(2L, "USER");

        when(artworkService.findById(1L))
                .thenReturn(createArtwork(owner));
        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(otherUser);

        ResponseEntity<Artwork> response =
                artworkController.update(1L, new Artwork(), principal);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(artworkService, never()).update(any(), any());
    }

    @Test
    void update_shouldReturn200WhenOwner() {
        User owner = createUser(1L, "USER");
        Artwork input = new Artwork();
        input.setTitle("Updated Sunset");

        when(artworkService.findById(1L))
                .thenReturn(createArtwork(owner));
        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(owner);
        when(artworkService.update(1L, input))
                .thenReturn(input);

        ResponseEntity<Artwork> response =
                artworkController.update(1L, input, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void update_shouldReturn200WhenAdmin() {
        User owner = createUser(1L, "USER");
        User admin = createUser(2L, "ADMIN");
        Artwork input = new Artwork();

        when(artworkService.findById(1L))
                .thenReturn(createArtwork(owner));
        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(admin);
        when(artworkService.update(1L, input))
                .thenReturn(input);

        ResponseEntity<Artwork> response =
                artworkController.update(1L, input, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void update_shouldReturn400WhenServiceRejectsUpdate() {
        User owner = createUser(1L, "USER");
        Artwork input = new Artwork();

        when(artworkService.findById(1L))
                .thenReturn(createArtwork(owner));
        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(owner);
        when(artworkService.update(1L, input))
                .thenReturn(null);

        ResponseEntity<Artwork> response =
                artworkController.update(1L, input, principal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void delete_shouldReturn404WhenArtworkNotFound() {
        when(artworkService.findById(99L))
                .thenReturn(null);

        ResponseEntity<Void> response =
                artworkController.delete(99L, principal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void delete_shouldReturn403WhenNotOwnerOrAdmin() {
        User owner = createUser(1L, "USER");
        User otherUser = createUser(2L, "USER");

        when(artworkService.findById(1L))
                .thenReturn(createArtwork(owner));
        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(otherUser);

        ResponseEntity<Void> response =
                artworkController.delete(1L, principal);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(artworkService, never()).delete(any());
    }

    @Test
    void delete_shouldReturn204WhenOwner() {
        User owner = createUser(1L, "USER");

        when(artworkService.findById(1L))
                .thenReturn(createArtwork(owner));
        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(owner);
        when(artworkService.delete(1L))
                .thenReturn(true);

        ResponseEntity<Void> response =
                artworkController.delete(1L, principal);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void delete_shouldReturn204WhenAdmin() {
        User owner = createUser(1L, "USER");
        User admin = createUser(2L, "ADMIN");

        when(artworkService.findById(1L))
                .thenReturn(createArtwork(owner));
        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(admin);
        when(artworkService.delete(1L))
                .thenReturn(true);

        ResponseEntity<Void> response =
                artworkController.delete(1L, principal);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void delete_shouldReturn404WhenDeletionFails() {
        User owner = createUser(1L, "USER");

        when(artworkService.findById(1L))
                .thenReturn(createArtwork(owner));
        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(owner);
        when(artworkService.delete(1L))
                .thenReturn(false);

        ResponseEntity<Void> response =
                artworkController.delete(1L, principal);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void uploadImage_shouldReturn200WhenSuccessful() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "art.png", "image/png", new byte[]{1, 2, 3}
        );

        when(fileStorageService.saveFile(file))
                .thenReturn("/uploads/test.png");

        ResponseEntity<String> response =
                artworkController.uploadImage(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("/uploads/test.png", response.getBody());
    }

    @Test
    void uploadImage_shouldReturn400ForInvalidFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "bad.txt", "text/plain", new byte[]{1}
        );

        when(fileStorageService.saveFile(file))
                .thenThrow(new IllegalArgumentException("Invalid image"));

        ResponseEntity<String> response =
                artworkController.uploadImage(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid image", response.getBody());
    }

    @Test
    void uploadImage_shouldReturn500ForIOException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "art.png", "image/png", new byte[]{1}
        );

        when(fileStorageService.saveFile(file))
                .thenThrow(new IOException("Disk error"));

        ResponseEntity<String> response =
                artworkController.uploadImage(file);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );
    }

    @Test
    void createWithImage_shouldReturn201WhenSuccessful() throws Exception {
        User user = createUser(1L, "USER");
        MockMultipartFile file = new MockMultipartFile(
                "file", "art.png", "image/png", new byte[]{1}
        );

        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(user);
        when(fileStorageService.saveFile(file))
                .thenReturn("/uploads/art.png");
        when(artworkService.create(any(Artwork.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Artwork> response =
                artworkController.createWithImage(
                        "Sunset", "A painting", 1L, file, principal
                );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sunset", response.getBody().getTitle());
        assertEquals("/uploads/art.png", response.getBody().getImagePath());
        assertEquals(1L, response.getBody().getCategory().getId());
        assertSame(user, response.getBody().getUser());
    }

    @Test
    void createWithImage_shouldReturn400ForInvalidFile() throws Exception {
        User user = createUser(1L, "USER");
        MockMultipartFile file = new MockMultipartFile(
                "file", "bad.txt", "text/plain", new byte[]{1}
        );

        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(user);
        when(fileStorageService.saveFile(file))
                .thenThrow(new IllegalArgumentException("Invalid file"));

        ResponseEntity<Artwork> response =
                artworkController.createWithImage(
                        "Sunset", "A painting", 1L, file, principal
                );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(artworkService, never()).create(any());
    }

    @Test
    void createWithImage_shouldReturn400WhenCreationFails() throws Exception {
        User user = createUser(1L, "USER");
        MockMultipartFile file = new MockMultipartFile(
                "file", "art.png", "image/png", new byte[]{1}
        );

        when(principal.getName())
                .thenReturn("artist@example.com");
        when(userService.findByEmail("artist@example.com"))
                .thenReturn(user);
        when(fileStorageService.saveFile(file))
                .thenReturn("/uploads/art.png");
        when(artworkService.create(any(Artwork.class)))
                .thenReturn(null);

        ResponseEntity<Artwork> response =
                artworkController.createWithImage(
                        "Sunset", "A painting", 1L, file, principal
                );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
