package com.carocart.productservice.service;

import com.carocart.productservice.dto.AdminResponseDTO;
import com.carocart.productservice.entity.Product;
import com.carocart.productservice.feign.AdminClient;
import com.carocart.productservice.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminClient adminClient;

    // Helper method to validate if admin response is valid and has ADMIN role
    private boolean isAdmin(AdminResponseDTO admin) {
        return admin != null && "ADMIN".equals(admin.getRole());
    }

    // Validate admin using token
    private void validateAdmin(String token) {
        AdminResponseDTO admin = adminClient.getCurrentAdmin(token);
        if (!isAdmin(admin)) {
            throw new RuntimeException("Unauthorized: Admin role required");
        }
    }

    @Override
    public Product addProduct(Product product, String token) {
        AdminResponseDTO admin = adminClient.getCurrentAdmin(token);

        if (!isAdmin(admin)) {
            throw new RuntimeException("Unauthorized: Admin role required");
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct, String token) {
        validateAdmin(token);
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setBrand(updatedProduct.getBrand());
            product.setCategory(updatedProduct.getCategory());
            product.setPrice(updatedProduct.getPrice());
            product.setMrp(updatedProduct.getMrp());
            product.setDiscount(updatedProduct.getDiscount());
            product.setStock(updatedProduct.getStock());
            product.setUnit(updatedProduct.getUnit());
            product.setImageUrl(updatedProduct.getImageUrl());
            product.setIsAvailable(updatedProduct.getIsAvailable());
            product.setUpdatedAt(LocalDateTime.now());
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id, String token) {
        validateAdmin(token);
        productRepository.deleteById(id);
    }
}
