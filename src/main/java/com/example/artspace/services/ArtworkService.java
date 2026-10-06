package com.example.artspace.services;

import com.example.artspace.models.Artwork;
import com.example.artspace.repositories.ArtworkRepository;
import org.springframework.stereotype.Service;
import com.example.artspace.repositories.CategoryRepository;
import com.example.artspace.repositories.UserRepository;

import java.util.List;

@Service
public class ArtworkService {
    private final ArtworkRepository artworkRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ArtworkService(ArtworkRepository artworkRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.artworkRepository = artworkRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<Artwork> findAll() {
        return artworkRepository.findAll();
    }

    public Artwork findById(Long id) {
        return artworkRepository.findById(id).orElse(null);
    }

    public List<Artwork> findByUserId(Long userId) {
        return artworkRepository.findByUserId(userId);
    }

    public List<Artwork> findByCategoryId(Long categoryId) {
        return artworkRepository.findByCategoryId(categoryId);
    }
}
