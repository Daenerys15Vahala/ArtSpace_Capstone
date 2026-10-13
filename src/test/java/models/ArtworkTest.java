package com.example.artspace.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ArtworkTest {

    @Test
    void constructorShouldSetArtworkDetails() {
        User user = new User();
        Category category = new Category();

        Artwork artwork = new Artwork(
                "Colorful Painting",
                "A painting with bright colors",
                "/uploads/painting.jpg",
                user,
                category
        );

        assertEquals("Colorful Painting", artwork.getTitle());
        assertEquals("A painting with bright colors", artwork.getDescription());
        assertEquals("/uploads/painting.jpg", artwork.getImagePath());
        assertSame(user, artwork.getUser());
        assertSame(category, artwork.getCategory());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        Artwork artwork = new Artwork();
        LocalDateTime date = LocalDateTime.of(2026, 10, 12, 12, 0);

        artwork.setCreatedAt(date);

        assertEquals(date, artwork.getCreatedAt());
    }

    @Test
    void onCreateShouldSetCreatedAt() {
        Artwork artwork = new Artwork();

        artwork.onCreate();

        assertNotNull(artwork.getCreatedAt());
    }
}