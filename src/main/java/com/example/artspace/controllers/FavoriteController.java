package com.example.artspace.controllers;


import com.example.artspace.models.Favorite;
import com.example.artspace.services.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import com.example.artspace.models.User;
import com.example.artspace.services.UserService;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    public FavoriteController(FavoriteService favoriteService, UserService userService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
    }

    @GetMapping
    public List<Map<String, Object>> findMyFavorites(Principal principal) {User user = userService.findByEmail(principal.getName());
        return favoriteService.findByUserId(user.getId())
                .stream()
                .map(favorite -> Map.<String, Object>of(
                        "id", favorite.getId(),
                        "artworkId", favorite.getArtwork().getId())).toList();
    }

    @PostMapping("/artwork/{artworkId}")
    public ResponseEntity<Map<String, Object>> create(
            @PathVariable Long artworkId,
            Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Favorite saved = favoriteService.create(user.getId(), artworkId);
        if (saved == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(Map.of(
                        "id", saved.getId(),
                        "artworkId", saved.getArtwork().getId()));
    }

    @DeleteMapping("/artwork/{artworkId}")
    public ResponseEntity<Void> delete(@PathVariable Long artworkId, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        boolean deleted = favoriteService.delete(user.getId(), artworkId);
        if(!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
