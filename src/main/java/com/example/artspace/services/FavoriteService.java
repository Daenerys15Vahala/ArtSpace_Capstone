package com.example.artspace.services;

import com.example.artspace.repositories.FavoriteRepository;
import org.springframework.stereotype.Service;
import com.example.artspace.models.Favorite;
import java.util.List;
import com.example.artspace.models.User;
import com.example.artspace.models.Artwork;
import com.example.artspace.repositories.UserRepository;
import com.example.artspace.repositories.ArtworkRepository;


@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, ArtworkRepository artworkRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.artworkRepository = artworkRepository;
    }

    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public Favorite create(Long userId, Long artworkId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        Artwork artwork = artworkRepository.findById(artworkId).orElse(null);
        if (artwork == null) {
            return null;
        }

        Favorite existing = favoriteRepository.findByUserIdAndArtworkId(userId, artworkId).orElse(null);
        if (existing != null) {
            return existing;
        }
        Favorite favorite = new Favorite(user, artwork);
        return favoriteRepository.save(favorite);
    }

    public boolean delete(Long userId, Long artworkId) {
        Favorite favorite = favoriteRepository.findByUserIdAndArtworkId(userId, artworkId).orElse(null);
        if (favorite == null) {
            return false;
        }
        favoriteRepository.delete(favorite);
        return true;
    }
}
