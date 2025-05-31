package com.carocart.productservice.controller;

import com.carocart.productservice.dto.ProductDTO;
import com.carocart.productservice.entity.Product;
import com.carocart.productservice.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;  // For JSON to Object conversion

    // 🟢 Admin: Add a product with image upload (multipart)
    @PostMapping(value = "/admin/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(@RequestPart("product") String productJson,
                                              @RequestPart(value = "image", required = false) MultipartFile image,
                                              @RequestHeader("Authorization") String token) throws IOException {

        // Convert JSON string to Product object
        Product product = objectMapper.readValue(productJson, Product.class);

        if (image != null && !image.isEmpty()) {
            product.setImage(image.getBytes());
        }

        Product saved = productService.addProduct(product, token);
        return ResponseEntity.ok(saved);
    }

    // 🟢 Admin: Update a product with optional image update
    @PutMapping(value = "/admin/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestPart("product") String productJson,
                                           @RequestPart(value = "image", required = false) MultipartFile image,
                                           @RequestHeader("Authorization") String token) throws IOException {

        Product updatedProduct = objectMapper.readValue(productJson, Product.class);

        if (image != null && !image.isEmpty()) {
            updatedProduct.setImage(image.getBytes());
        }

        Product updated = productService.updateProduct(id, updatedProduct, token);
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
    
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null || product.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)  // or detect actual type
            .body(product.getImage());
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
    
    @GetMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<List<Product>> getProductsBySubCategory(@PathVariable Long subCategoryId) {
        return ResponseEntity.ok(productService.getProductsBySubCategory(subCategoryId));
    }
    

    @GetMapping("/dto/{id}")
    public ResponseEntity<ProductDTO> getProductDTOById(@PathVariable Long id) {
        ProductDTO dto = productService.getProductDTOById(id);
        return ResponseEntity.ok(dto);
    }


}
