package com.example.artspace.repositories;

import com.example.artspace.models.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

    List<Artwork> findByUserId(Long userId);

    List<Artwork> findByCategoryId(Long categoryId);
}
