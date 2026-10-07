package com.example.artspace.controllers;


import com.example.artspace.models.Favorite;
import com.example.artspace.services.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/user/{userId}")
    public List<Favorite> findByUserId(@PathVariable Long userId) {
        return favoriteService.findByUserId(userId);
    }

    @PostMapping("/user/{userId}/artwork/{artworkId}")
    public ResponseEntity<Favorite> addFavorite(@PathVariable Long userId, @PathVariable Long artworkId) {
        Favorite saved = favoriteService.create(userId, artworkId);
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(saved);
    }

    @DeleteMapping("/user/{userId}/artwork/{artworkId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long artworkId) {
        boolean deleted = favoriteService.delete(userId, artworkId);
        if(!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
