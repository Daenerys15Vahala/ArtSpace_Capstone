package com.example.artspace.controllers;


import com.example.artspace.models.Artwork;
import com.example.artspace.services.ArtworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {

    private final ArtworkService artworkService;

    public ArtworkController(ArtworkService artworkService) {
        this.artworkService = artworkService;
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
    public ResponseEntity<Artwork> create(@RequestBody Artwork input) {
        Artwork saved = artworkService.create(input);
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artwork> update(@PathVariable Long id, @RequestBody Artwork input) {
        Artwork updated = artworkService.update(id, input);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = artworkService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}