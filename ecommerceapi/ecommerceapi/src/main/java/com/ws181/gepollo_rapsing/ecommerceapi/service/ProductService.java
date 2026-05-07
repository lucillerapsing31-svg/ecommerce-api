package com.ws181.gepollo_rapsing.ecommerceapi.service;

import com.ws181.gepollo_rapsing.ecommerceapi.dto.CreateProductDto;
import com.ws181.gepollo_rapsing.ecommerceapi.model.Category;
import com.ws181.gepollo_rapsing.ecommerceapi.model.Product;
import com.ws181.gepollo_rapsing.ecommerceapi.repository.CategoryRepository;
import com.ws181.gepollo_rapsing.ecommerceapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling business logic related to Products.
 * Uses Spring Data JPA Repository for data persistence.
 */
@Service
public class ProductService {

    // Inject Repositories
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // Constructor injection
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves all products from the database.
     * @return List of all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Finds a product by its ID.
     * @param id The product ID.
     * @return Optional containing the product if found.
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Saves a new product from DTO data.
     * @param dto The data transfer object containing product info.
     * @return The saved product with generated ID.
     */
    public Product createProduct(CreateProductDto dto) {
        // Find Category by ID
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));

        // Create new Product object
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setStockQuantity(10); // Default stock
        product.setImageUrl(dto.getImageUrl() != null ? dto.getImageUrl() : "");
        product.setCategory(category);

        return productRepository.save(product);
    }

    /**
     * Updates an existing product.
     * @param id The ID of the product to update.
     * @param updatedProduct The new product data.
     * @return The updated product, or empty if not found.
     */
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    updatedProduct.setId(id);
                    return productRepository.save(updatedProduct);
                });
    }

    /**
     * Deletes a product by its ID.
     * @param id The product ID.
     * @return true if deleted successfully.
     */
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Filters products by category name using method naming convention.
     * @param categoryName The name of the category.
     * @return List of products in that category.
     */
    public List<Product> filterByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    /**
     * Filters products within a price range.
     * @param min Minimum price.
     * @param max Maximum price.
     * @return List of products in price range.
     */
    public List<Product> filterByPrice(double min, double max) {
        return productRepository.findByPriceBetween(min, max);
    }

    /**
     * Searches products by name containing keyword.
     * @param keyword Search keyword.
     * @return List of matching products.
     */
    public List<Product> filterByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}