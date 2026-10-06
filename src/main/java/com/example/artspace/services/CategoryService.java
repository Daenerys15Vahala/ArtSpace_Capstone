package com.example.artspace.services;

import com.example.artspace.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import com.example.artspace.models.Category;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category input) {
        Category existing = findById(id);

        if (existing == null) {
            return null;
        }
        existing.setName(input.getName());
        return categoryRepository.save(existing);
    }

    public boolean delete(Long id) {
        Category existing = findById(id);
        if (existing == null) {
            return false;
        }
        categoryRepository.delete(existing);
        return true;
    }
}