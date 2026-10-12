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

        // Arrange: Create a user and category
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(1L);

        Artwork artwork = new Artwork();
        artwork.setTitle("Sunset Painting");
        artwork.setImagePath("/uploads/test-image.jpg");
        artwork.setUser(user);
        artwork.setCategory(category);

        // Simulate finding the user and category
        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));

        when(categoryRepository.findById(1L))
                .thenReturn(java.util.Optional.of(category));

        // Simulate a valid uploaded image
        when(fileStorageService.isValidImagePath(artwork.getImagePath()))
                .thenReturn(true);

        // Simulate saving the artwork
        when(artworkRepository.save(artwork))
                .thenReturn(artwork);

        // Act
        Artwork result = artworkService.create(artwork);

        // Assert
        assertNotNull(result);
        assertEquals("Sunset Painting", result.getTitle());

        verify(artworkRepository).save(artwork);
    }

    @Test
    void create_shouldReturnNullWhenUserDoesNotExist() {

        // Arrange: Create an artwork with a user and category
        User user = new User();
        user.setId(999L);

        Category category = new Category();
        category.setId(1L);

        Artwork artwork = new Artwork();
        artwork.setTitle("Sunset Painting");
        artwork.setImagePath("/uploads/test-image.jpg");
        artwork.setUser(user);
        artwork.setCategory(category);

        // Simulate a user that doesn't exist in the database
        when(userRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        // Act: Attempt to create the artwork
        Artwork result = artworkService.create(artwork);

        // Assert: The artwork should not be created
        assertNull(result);

        // Verify that no artwork was saved
        verify(artworkRepository, never()).save(any(Artwork.class));
    }

    @Test
    void create_shouldReturnNullWhenCategoryDoesNotExist() {

        // Arrange: Create a valid user
        User user = new User();
        user.setId(1L);

        // Create a category that doesn't exist
        Category category = new Category();
        category.setId(999L);

        Artwork artwork = new Artwork();
        artwork.setTitle("Ocean Painting");
        artwork.setImagePath("/uploads/test-image.jpg");
        artwork.setUser(user);
        artwork.setCategory(category);

        // Simulate an existing user
        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));

        // Simulate a category that doesn't exist
        when(categoryRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        // Act
        Artwork result = artworkService.create(artwork);

        // Assert
        assertNull(result);

        // Verify that nothing was saved
        verify(artworkRepository, never()).save(any(Artwork.class));
    }

    @Test
    void create_shouldReturnNullWhenImagePathIsInvalid() {

        // Arrange: Create a valid user and category
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(1L);

        Artwork artwork = new Artwork();
        artwork.setTitle("Ocean Painting");
        artwork.setImagePath("/uploads/invalid-image.jpg");
        artwork.setUser(user);
        artwork.setCategory(category);

        // Simulate an existing user
        when(userRepository.findById(1L))
                .thenReturn(java.util.Optional.of(user));

        // Simulate an existing category
        when(categoryRepository.findById(1L))
                .thenReturn(java.util.Optional.of(category));

        // Simulate an invalid image path
        when(fileStorageService.isValidImagePath(artwork.getImagePath()))
                .thenReturn(false);

        // Act: Attempt to create the artwork
        Artwork result = artworkService.create(artwork);

        // Assert: Artwork creation should fail
        assertNull(result);

        // Verify that nothing was saved
        verify(artworkRepository, never()).save(any(Artwork.class));
    }

    @Test
    void update_shouldUpdateExistingArtwork() {

        // Arrange: Create an existing artwork
        Artwork existing = new Artwork();
        existing.setId(1L);
        existing.setTitle("Old Title");
        existing.setDescription("Old Description");

        // Create the updated information
        Artwork updatedInfo = new Artwork();
        updatedInfo.setTitle("New Sunset Painting");
        updatedInfo.setDescription("A beautiful sunset");

        // Simulate finding the existing artwork
        when(artworkRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        // Simulate saving the updated artwork
        when(artworkRepository.save(existing))
                .thenReturn(existing);

        // Act: Update the artwork
        Artwork result = artworkService.update(1L, updatedInfo);

        // Assert: Verify the updated information
        assertNotNull(result);
        assertEquals("New Sunset Painting", result.getTitle());
        assertEquals("A beautiful sunset", result.getDescription());

        // Verify that the artwork was saved
        verify(artworkRepository).save(existing);
    }

    @Test
    void update_shouldReturnNullWhenArtworkDoesNotExist() {

        // Arrange: Create updated artwork information
        Artwork updatedInfo = new Artwork();
        updatedInfo.setTitle("New Painting");
        updatedInfo.setDescription("Updated description");

        // Simulate an artwork that doesn't exist
        when(artworkRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        // Act: Attempt to update the artwork
        Artwork result = artworkService.update(999L, updatedInfo);

        // Assert: The update should fail
        assertNull(result);

        // Verify that nothing was saved
        verify(artworkRepository, never()).save(any(Artwork.class));
    }

    @Test
    void update_shouldChangeArtworkCategory() {

        // Arrange: Create an existing artwork
        Artwork existing = new Artwork();
        existing.setId(1L);
        existing.setTitle("Sunset Painting");

        // Create a new category
        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("Photography");

        // Create the updated information
        Artwork updatedInfo = new Artwork();
        updatedInfo.setTitle("Sunset Photo");
        updatedInfo.setDescription("A sunset photograph");
        updatedInfo.setCategory(newCategory);

        // Simulate finding the artwork
        when(artworkRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        // Simulate finding the new category
        when(categoryRepository.findById(2L))
                .thenReturn(java.util.Optional.of(newCategory));

        // Simulate saving the updated artwork
        when(artworkRepository.save(existing))
                .thenReturn(existing);

        // Act: Update the artwork
        Artwork result = artworkService.update(1L, updatedInfo);

        // Assert: Verify the category changed
        assertNotNull(result);
        assertEquals("Photography", result.getCategory().getName());
        assertEquals("Sunset Photo", result.getTitle());

        // Verify the repository saved the changes
        verify(artworkRepository).save(existing);
    }

    @Test
    void update_shouldReturnNullWhenCategoryDoesNotExist() {

        // Arrange: Create an existing artwork
        Artwork existing = new Artwork();
        existing.setId(1L);
        existing.setTitle("Sunset Painting");

        // Create a category that doesn't exist
        Category invalidCategory = new Category();
        invalidCategory.setId(999L);

        Artwork updatedInfo = new Artwork();
        updatedInfo.setTitle("Updated Painting");
        updatedInfo.setCategory(invalidCategory);

        // Simulate finding the existing artwork
        when(artworkRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        // Simulate a category that doesn't exist
        when(categoryRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        // Act: Attempt to update the artwork
        Artwork result = artworkService.update(1L, updatedInfo);

        // Assert: The update should fail
        assertNull(result);

        // Verify that nothing was saved
        verify(artworkRepository, never()).save(any(Artwork.class));

        // Verify the original title remains unchanged
        assertEquals("Sunset Painting", existing.getTitle());
    }

    @Test
    void delete_shouldDeleteExistingArtwork() {

        // Arrange: Create an existing artwork
        Artwork artwork = new Artwork();
        artwork.setId(1L);
        artwork.setTitle("Sunset Painting");

        // Simulate finding the artwork
        when(artworkRepository.findById(1L))
                .thenReturn(java.util.Optional.of(artwork));

        // Act: Delete the artwork
        boolean result = artworkService.delete(1L);

        // Assert: Deletion should succeed
        assertTrue(result);

        // Verify that the repository deleted the artwork
        verify(artworkRepository).delete(artwork);
    }

    @Test
    void delete_shouldReturnFalseWhenArtworkDoesNotExist() {

        // Arrange: Simulate an artwork that doesn't exist
        when(artworkRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        // Act: Attempt to delete the artwork
        boolean result = artworkService.delete(999L);

        // Assert: Deletion should fail
        assertFalse(result);

        // Verify that no artwork was deleted
        verify(artworkRepository, never())
                .delete(any(Artwork.class));
    }
}