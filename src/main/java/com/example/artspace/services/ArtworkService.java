package com.example.artspace.services;

import com.example.artspace.models.Artwork;
import com.example.artspace.repositories.ArtworkRepository;
import org.springframework.stereotype.Service;
import com.example.artspace.repositories.CategoryRepository;
import com.example.artspace.repositories.UserRepository;
import com.example.artspace.models.User;
import com.example.artspace.models.Category;

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

    public Artwork create(Artwork input) {
        if (input.getUser() == null || input.getCategory() == null) {
            return null;
        }
        User user = userRepository.findById(input.getUser().getId()).orElse(null);
        if (user == null) {
            return null;
        }
        Category category = categoryRepository.findById(input.getCategory().getId()).orElse(null);
        if (category == null) {
            return null;
        }
        input.setUser(user);
        input.setCategory(category);
        return artworkRepository.save(input);
    }

    public Artwork update(Long id, Artwork input) {
        Artwork existing = findById(id);

        if (existing == null) {
            return null;
        }
        if (input.getCategory() != null) {
            Category category = categoryRepository.findById(input.getCategory().getId()).orElse(null);
            if (category == null) {
                return null;
            }
            existing.setCategory(category);
        }

        existing.setTitle(input.getTitle());
        existing.setDescription(input.getDescription());
        existing.setImagePath(input.getImagePath());
        return artworkRepository.save(existing);
    }

    public boolean delete(Long id) {
        Artwork existing = findById(id);

        if (existing == null) {
            return false;
        }
        artworkRepository.delete(existing);
        return true;
    }
}
