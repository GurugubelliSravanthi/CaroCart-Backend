package com.carocart.productservice.controller;

import com.carocart.productservice.entity.Product;
import com.carocart.productservice.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 🟢 Admin: Add a product
    @PostMapping("/admin/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product,
                                              @RequestHeader("Authorization") String token) {
        Product saved = productService.addProduct(product, token);
        return ResponseEntity.ok(saved);
    }

    // 🟢 Admin: Update a product
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody Product product,
                                           @RequestHeader("Authorization") String token) {
        Product updated = productService.updateProduct(id, product, token);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // 🟢 Admin: Delete a product
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,
                                                @RequestHeader("Authorization") String token) {
        productService.deleteProduct(id, token);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // 🟢 User: Get all products
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // 🟢 User: Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
}
