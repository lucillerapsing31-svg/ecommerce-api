package com.ws181.gepollo_rapsing.ecommerceapi.controller;

import com.ws181.gepollo_rapsing.ecommerceapi.model.Product;
import com.ws181.gepollo_rapsing.ecommerceapi.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "*") // ADD THIS LINE HERE
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // GET all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // GET single product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET filter products
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam String filterType,
            @RequestParam String filterValue) {

        List<Product> result;

        switch (filterType.toLowerCase()) {
            case "category":
                result = productService.filterByCategory(filterValue);
                break;
            case "name":
                result = productService.filterByName(filterValue);
                break;
            case "price":
                // Assuming filterValue is like "min,max"
                String[] range = filterValue.split(",");
                double min = Double.parseDouble(range[0]);
                double max = range.length > 1 ? Double.parseDouble(range[1]) : Double.MAX_VALUE;
                result = productService.filterByPrice(min, max);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    // POST create new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product created = productService.createProduct(product);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // PUT update entire product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH partially update product
    @PatchMapping("/{id}")
    public ResponseEntity<Product> partialUpdateProduct(@PathVariable Long id, @RequestBody Product partialProduct) {
        return productService.getProductById(id)
                .map(existing -> {
                    if (partialProduct.getName() != null) existing.setName(partialProduct.getName());
                    if (partialProduct.getDescription() != null) existing.setDescription(partialProduct.getDescription());
                    if (partialProduct.getPrice() != 0) existing.setPrice(partialProduct.getPrice());
                    if (partialProduct.getCategory() != null) existing.setCategory(partialProduct.getCategory());
                    if (partialProduct.getStockQuantity() != 0) existing.setStockQuantity(partialProduct.getStockQuantity());
                    if (partialProduct.getImageUrl() != null) existing.setImageUrl(partialProduct.getImageUrl());

                    return productService.updateProduct(id, existing).orElse(null);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
