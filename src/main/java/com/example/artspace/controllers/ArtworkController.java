package com.example.artspace.controllers;


import com.example.artspace.models.Artwork;
import com.example.artspace.models.Category;
import com.example.artspace.services.ArtworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import com.example.artspace.models.User;
import com.example.artspace.services.UserService;
import com.example.artspace.services.FileStorageService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    private final ArtworkService artworkService;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    public ArtworkController(ArtworkService artworkService, UserService userService, FileStorageService fileStorageService) {
        this.artworkService = artworkService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public List<Artwork> findAll() {
        return artworkService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artwork> findById(@PathVariable Long id) {
        Artwork artwork = artworkService.findById(id);
        if (artwork == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(artwork);
    }

    @GetMapping("/category/{categoryId}")
    public List<Artwork> findByCategoryId(@PathVariable Long categoryId) {
        return artworkService.findByCategoryId(categoryId);
    }

    @GetMapping("/user/{userId}")
    public List<Artwork> findByUserId(@PathVariable Long userId) {
        return artworkService.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Artwork> create(@RequestBody Artwork input, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        input.setUser(user);
        Artwork saved = artworkService.create(input);
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artwork> update(@PathVariable Long id, @RequestBody Artwork input, Principal principal) {
        Artwork existing = artworkService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        User loggedInUser = userService.findByEmail(principal.getName());
        boolean isOwner = existing.getUser().getId().equals(loggedInUser.getId());
        boolean isAdmin = loggedInUser.getRole().equals("ADMIN");
        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(403).build();
        }
        Artwork updated = artworkService.update(id, input);
        if (updated == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        Artwork existing = artworkService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        User loggedInUser = userService.findByEmail(principal.getName());
        boolean isOwner = existing.getUser().getId().equals(loggedInUser.getId());
        boolean isAdmin = loggedInUser.getRole().equals("ADMIN");
        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(403).build();
        }
        boolean deleted = artworkService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file) {
        try {
            String imagePath = fileStorageService.saveFile(file);
            return ResponseEntity.ok(imagePath);
        } catch (IOException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/with-image")
    public ResponseEntity<Artwork> createWithImage(
            @RequestParam("title") String title, @RequestParam(value = "description", required = false) String description, @RequestParam("categoryId") Long categoryId, @RequestParam("file") MultipartFile file, Principal principal) throws IOException {

        User user = userService.findByEmail(principal.getName());
        String imagePath = fileStorageService.saveFile(file);
        Artwork input = new Artwork();
        input.setTitle(title);
        input.setDescription(description);
        input.setImagePath(imagePath);
        input.setUser(user);
        Category category = new Category();
        category.setId(categoryId);
        input.setCategory(category);
        Artwork saved = artworkService.create(input);
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(saved);
    }
}

