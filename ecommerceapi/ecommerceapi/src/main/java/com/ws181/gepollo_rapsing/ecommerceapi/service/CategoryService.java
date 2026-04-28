package com.ws181.gepollo_rapsing.ecommerceapi.service;

import com.ws181.gepollo_rapsing.ecommerceapi.model.Category;
import com.ws181.gepollo_rapsing.ecommerceapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get category by ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Create new category
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Update category
    public Optional<Category> updateCategory(Long id, Category updatedCategory) {
        return categoryRepository.findById(id)
                .map(existing -> {
                    updatedCategory.setId(id);
                    return categoryRepository.save(updatedCategory);
                });
    }

    // Delete category
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}