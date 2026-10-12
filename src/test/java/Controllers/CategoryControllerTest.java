
package Controllers;

import com.example.artspace.controllers.CategoryController;
import com.example.artspace.models.Category;
import com.example.artspace.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void findAll_shouldReturnAllCategories() {
        Category category = new Category();
        category.setName("Painting");

        when(categoryService.findAll())
                .thenReturn(List.of(category));

        List<Category> result = categoryController.findAll();

        assertEquals(1, result.size());
        assertEquals("Painting", result.get(0).getName());
        verify(categoryService).findAll();
    }

    @Test
    void findById_shouldReturn200WhenCategoryExists() {
        Category category = new Category();
        category.setName("Photography");

        when(categoryService.findById(1L))
                .thenReturn(category);

        ResponseEntity<Category> response =
                categoryController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Photography", response.getBody().getName());
    }

    @Test
    void findById_shouldReturn404WhenCategoryNotFound() {
        when(categoryService.findById(99L))
                .thenReturn(null);

        ResponseEntity<Category> response =
                categoryController.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void create_shouldReturn201WhenCategoryCreated() {
        Category input = new Category();
        input.setName("Drawing");

        when(categoryService.create(input))
                .thenReturn(input);

        ResponseEntity<Category> response =
                categoryController.create(input);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Drawing", response.getBody().getName());

        verify(categoryService).create(input);
    }

    @Test
    void update_shouldReturn200WhenCategoryUpdated() {
        Category input = new Category();
        input.setName("Digital Art");

        when(categoryService.update(1L, input))
                .thenReturn(input);

        ResponseEntity<Category> response =
                categoryController.update(1L, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Digital Art", response.getBody().getName());
    }

    @Test
    void update_shouldReturn404WhenCategoryNotFound() {
        Category input = new Category();
        input.setName("Sculpture");

        when(categoryService.update(99L, input))
                .thenReturn(null);

        ResponseEntity<Category> response =
                categoryController.update(99L, input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void delete_shouldReturn204WhenCategoryDeleted() {
        when(categoryService.delete(1L))
                .thenReturn(true);

        ResponseEntity<Void> response =
                categoryController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService).delete(1L);
    }

    @Test
    void delete_shouldReturn404WhenCategoryNotFound() {
        when(categoryService.delete(99L))
                .thenReturn(false);

        ResponseEntity<Void> response =
                categoryController.delete(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(categoryService).delete(99L);
    }
}
