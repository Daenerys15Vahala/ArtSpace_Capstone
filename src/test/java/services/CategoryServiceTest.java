package com.example.artspace.services;

import com.example.artspace.models.Category;
import com.example.artspace.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void findAll_shouldReturnAllCategories() {
        Category category = new Category();
        category.setName("Painting");

        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<Category> result = categoryService.findAll();

        assertEquals(1, result.size());
        assertEquals("Painting", result.get(0).getName());
        verify(categoryRepository).findAll();
    }

    @Test
    void findById_shouldReturnCategoryWhenItExists() {
        Category category = new Category();
        category.setName("Photography");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        Category result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals("Photography", result.getName());
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {
        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        Category result = categoryService.findById(99L);

        assertNull(result);
    }

    @Test
    void create_shouldSaveCategory() {
        Category category = new Category();
        category.setName("Drawing");

        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.create(category);

        assertNotNull(result);
        assertEquals("Drawing", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void update_shouldUpdateExistingCategory() {
        Category existing = new Category();
        existing.setName("Painting");

        Category input = new Category();
        input.setName("Digital Art");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);

        Category result = categoryService.update(1L, input);

        assertNotNull(result);
        assertEquals("Digital Art", result.getName());
        verify(categoryRepository).save(existing);
    }

    @Test
    void update_shouldReturnNullWhenCategoryNotFound() {
        Category input = new Category();
        input.setName("Sculpture");

        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        Category result = categoryService.update(99L, input);

        assertNull(result);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteExistingCategory() {
        Category category = new Category();
        category.setName("Painting");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        boolean result = categoryService.delete(1L);

        assertTrue(result);
        verify(categoryRepository).delete(category);
    }

    @Test
    void delete_shouldReturnFalseWhenNotFound() {
        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        boolean result = categoryService.delete(99L);

        assertFalse(result);
        verify(categoryRepository, never()).delete(any());
    }
}
