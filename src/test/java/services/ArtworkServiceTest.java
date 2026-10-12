package com.example.artspace.services;

import com.example.artspace.models.Artwork;
import com.example.artspace.repositories.ArtworkRepository;
import com.example.artspace.repositories.CategoryRepository;
import com.example.artspace.repositories.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.artspace.models.User;
import com.example.artspace.models.Category;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtworkServiceTest {

    @Mock
    private ArtworkRepository artworkRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ArtworkService artworkService;

    @Test
    void findAll_shouldReturnAllArtworks() {
        Artwork artwork = new Artwork();
        when(artworkRepository.findAll())
                .thenReturn(List.of(artwork));
        List<Artwork> result = artworkService.findAll();
        assertEquals(1, result.size());
        verify(artworkRepository).findAll();
    }

    @Test
    void findById_shouldReturnNullWhenArtworkDoesNotExist() {
        when(artworkRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());
        Artwork result = artworkService.findById(999L);
        assertNull(result);
        verify(artworkRepository).findById(999L);
    }

    @Test
    void findById_shouldReturnArtworkWhenItExists() {
        Artwork artwork = new Artwork();
        artwork.setId(1L);
        artwork.setTitle("Sunset Painting");
        when(artworkRepository.findById(1L))
                .thenReturn(java.util.Optional.of(artwork));
        Artwork result = artworkService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Sunset Painting", result.getTitle());
        verify(artworkRepository).findById(1L);
    }

    @Test
    void findByUserId_shouldReturnUsersArtworks() {
        Artwork artwork1 = new Artwork();
        artwork1.setTitle("Sunset Painting");
        Artwork artwork2 = new Artwork();
        artwork2.setTitle("Ocean Drawing");
        when(artworkRepository.findByUserId(1L))
                .thenReturn(List.of(artwork1, artwork2));
        List<Artwork> result = artworkService.findByUserId(1L);
        assertEquals(2, result.size());
        assertEquals("Sunset Painting", result.get(0).getTitle());
        assertEquals("Ocean Drawing", result.get(1).getTitle());
        verify(artworkRepository).findByUserId(1L);
    }
    @Test
    void findByCategoryId_shouldReturnArtworksInCategory() {
        Artwork artwork = new Artwork();
        artwork.setTitle("Mountain Landscape");
        when(artworkRepository.findByCategoryId(1L))
                .thenReturn(List.of(artwork));
        List<Artwork> result = artworkService.findByCategoryId(1L);
        assertEquals(1, result.size());
        assertEquals("Mountain Landscape", result.get(0).getTitle());
        verify(artworkRepository).findByCategoryId(1L);
    }
    @Test
    void create_shouldSaveArtworkWhenValid() {

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(1L);

        Artwork artwork = new Artwork();
        artwork.setTitle("Sunset Painting");
        artwork.setImagePath("/uploads/test-image.jpg");
        artwork.setUser(user);
        artwork.setCategory(category);

        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));

        when(categoryRepository.findById(1L))
                .thenReturn(java.util.Optional.of(category));

        when(fileStorageService.isValidImagePath(artwork.getImagePath()))
                .thenReturn(true);

        when(artworkRepository.save(artwork))
                .thenReturn(artwork);
        Artwork result = artworkService.create(artwork);

        assertNotNull(result);
        assertEquals("Sunset Painting", result.getTitle());

        verify(artworkRepository).save(artwork);
    }

    @Test
    void create_shouldReturnNullWhenUserDoesNotExist() {

        User user = new User();
        user.setId(999L);

        Category category = new Category();
        category.setId(1L);

        Artwork artwork = new Artwork();
        artwork.setTitle("Sunset Painting");
        artwork.setImagePath("/uploads/test-image.jpg");
        artwork.setUser(user);
        artwork.setCategory(category);

        when(userRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());
        Artwork result = artworkService.create(artwork);

        assertNull(result);

        verify(artworkRepository, never()).save(any(Artwork.class));
    }

    @Test
    void create_shouldReturnNullWhenCategoryDoesNotExist() {

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(999L);

        Artwork artwork = new Artwork();
        artwork.setTitle("Ocean Painting");
        artwork.setImagePath("/uploads/test-image.jpg");
        artwork.setUser(user);
        artwork.setCategory(category);

        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));

        when(categoryRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        Artwork result = artworkService.create(artwork);

        assertNull(result);

        verify(artworkRepository, never()).save(any(Artwork.class));
    }

    @Test
    void create_shouldReturnNullWhenImagePathIsInvalid() {

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(1L);

        Artwork artwork = new Artwork();
        artwork.setTitle("Ocean Painting");
        artwork.setImagePath("/uploads/invalid-image.jpg");
        artwork.setUser(user);
        artwork.setCategory(category);

        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));

        when(categoryRepository.findById(1L))
                .thenReturn(java.util.Optional.of(category));

        when(fileStorageService.isValidImagePath(artwork.getImagePath()))
                .thenReturn(false);

        Artwork result = artworkService.create(artwork);

        assertNull(result);

        verify(artworkRepository, never()).save(any(Artwork.class));
    }

    @Test
    void update_shouldUpdateExistingArtwork() {

        Artwork existing = new Artwork();
        existing.setId(1L);
        existing.setTitle("Old Title");
        existing.setDescription("Old Description");

        Artwork updatedInfo = new Artwork();
        updatedInfo.setTitle("New Sunset Painting");
        updatedInfo.setDescription("A beautiful sunset");

        when(artworkRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        when(artworkRepository.save(existing))
                .thenReturn(existing);

        Artwork result = artworkService.update(1L, updatedInfo);

        assertNotNull(result);
        assertEquals("New Sunset Painting", result.getTitle());
        assertEquals("A beautiful sunset", result.getDescription());

        verify(artworkRepository).save(existing);
    }

    @Test
    void update_shouldReturnNullWhenArtworkDoesNotExist() {

        Artwork updatedInfo = new Artwork();
        updatedInfo.setTitle("New Painting");
        updatedInfo.setDescription("Updated description");

        when(artworkRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        Artwork result = artworkService.update(999L, updatedInfo);

        assertNull(result);

        verify(artworkRepository, never()).save(any(Artwork.class));
    }

    @Test
    void update_shouldChangeArtworkCategory() {

        Artwork existing = new Artwork();
        existing.setId(1L);
        existing.setTitle("Sunset Painting");

        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("Photography");

        Artwork updatedInfo = new Artwork();
        updatedInfo.setTitle("Sunset Photo");
        updatedInfo.setDescription("A sunset photograph");
        updatedInfo.setCategory(newCategory);

        when(artworkRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        when(categoryRepository.findById(2L))
                .thenReturn(java.util.Optional.of(newCategory));

        when(artworkRepository.save(existing))
                .thenReturn(existing);

        Artwork result = artworkService.update(1L, updatedInfo);

        assertNotNull(result);
        assertEquals("Photography", result.getCategory().getName());
        assertEquals("Sunset Photo", result.getTitle());

        verify(artworkRepository).save(existing);
    }

    @Test
    void update_shouldReturnNullWhenCategoryDoesNotExist() {

        Artwork existing = new Artwork();
        existing.setId(1L);
        existing.setTitle("Sunset Painting");

        Category invalidCategory = new Category();
        invalidCategory.setId(999L);

        Artwork updatedInfo = new Artwork();
        updatedInfo.setTitle("Updated Painting");
        updatedInfo.setCategory(invalidCategory);

        when(artworkRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        when(categoryRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        Artwork result = artworkService.update(1L, updatedInfo);

        assertNull(result);

        verify(artworkRepository, never()).save(any(Artwork.class));

        assertEquals("Sunset Painting", existing.getTitle());
    }

    @Test
    void delete_shouldDeleteExistingArtwork() {

        Artwork artwork = new Artwork();
        artwork.setId(1L);
        artwork.setTitle("Sunset Painting");

        when(artworkRepository.findById(1L))
                .thenReturn(java.util.Optional.of(artwork));

        boolean result = artworkService.delete(1L);

        assertTrue(result);

        verify(artworkRepository).delete(artwork);
    }

    @Test
    void delete_shouldReturnFalseWhenArtworkDoesNotExist() {

        when(artworkRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        boolean result = artworkService.delete(999L);

        assertFalse(result);

        verify(artworkRepository, never())
                .delete(any(Artwork.class));
    }
}