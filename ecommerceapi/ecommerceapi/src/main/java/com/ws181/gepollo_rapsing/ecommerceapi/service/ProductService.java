package com.ws181.gepollo_rapsing.ecommerceapi.service;

import com.ws181.gepollo_rapsing.ecommerceapi.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    // Store products in memory
    private final List<Product> products = new ArrayList<>();
    // Counter for generating unique IDs
    private Long nextId = 1L;

    // Initialize with at least 10 sample products
    public ProductService() {
        addSampleProducts();
    }

    private void addSampleProducts() {
        products.add(new Product(nextId++, "Laptop", "High-performance laptop", 59999.99, "Electronics", 10, "url1"));
        products.add(new Product(nextId++, "Smartphone", "Latest model smartphone", 29999.99, "Electronics", 25, "url2"));
        products.add(new Product(nextId++, "T-Shirt", "Cotton t-shirt", 499.99, "Clothing", 50, "url3"));
        products.add(new Product(nextId++, "Jeans", "Denim jeans", 999.99, "Clothing", 30, "url4"));
        products.add(new Product(nextId++, "Headphones", "Noise-cancelling headphones", 3499.99, "Electronics", 15, "url5"));
        products.add(new Product(nextId++, "Sneakers", "Running shoes", 2499.99, "Footwear", 20, "url6"));
        products.add(new Product(nextId++, "Watch", "Analog wrist watch", 1299.99, "Accessories", 12, "url7"));
        products.add(new Product(nextId++, "Backpack", "Waterproof backpack", 899.99, "Accessories", 18, "url8"));
        products.add(new Product(nextId++, "Refrigerator", "Double door fridge", 45999.99, "Appliances", 8, "url9"));
        products.add(new Product(nextId++, "Blender", "High-speed blender", 1999.99, "Appliances", 22, "url10"));
    }

    /**
     * Retrieves all products from the data source.
     *
     * @return List containing all Product objects.
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Finds a specific product by its unique ID.
     *
     * @param id The identifier of the product to find.
     * @return An Optional object containing the Product if found, otherwise empty.
     */
    public Optional<Product> getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Creates and saves a new product to the database.
     *
     * @param product The Product object containing details to be saved.
     * @return The newly created Product with generated ID.
     */
    public Product createProduct(Product product) {
        product.setId(nextId++);
        products.add(product);
        return product;
    }

    /**
     * Updates all details of an existing product.
     *
     * @param id The ID of the product to update.
     * @param updatedProduct The complete new data to replace the old one.
     * @return An Optional containing the updated Product if successful, or empty if not found.
     */
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            Product existing = products.get(i);
            if (existing.getId().equals(id)) {
                updatedProduct.setId(id);
                products.set(i, updatedProduct);
                return Optional.of(updatedProduct);
            }
        }
        return Optional.empty();
    }

    /**
     * Removes a product from the database.
     *
     * @param id The ID of the product to delete.
     * @return true if deleted successfully, false if product was not found.
     */
    public boolean deleteProduct(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    /**
     * Filters products to show only those belonging to a specific category.
     *
     * @param category The name of the category to match.
     * @return List of products matching the category.
     */
    public List<Product> filterByCategory(String category) {
        return products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Filters products whose price falls within the specified range.
     *
     * @param minPrice The minimum price threshold (inclusive).
     * @param maxPrice The maximum price threshold (inclusive).
     * @return List of products priced between minPrice and maxPrice.
     */
    public List<Product> filterByPrice(double minPrice, double maxPrice) {
        return products.stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Searches for products whose name contains the given keyword.
     *
     * @param keyword The search keyword or partial product name.
     * @return List of products whose name contains the keyword.
     */
    public List<Product> filterByName(String keyword) {
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}