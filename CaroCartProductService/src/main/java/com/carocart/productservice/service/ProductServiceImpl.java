package com.carocart.productservice.service;

import com.carocart.productservice.dto.AdminResponseDTO;
import com.carocart.productservice.dto.ProductDTO;
import com.carocart.productservice.entity.Product;
import com.carocart.productservice.feign.AdminClient;
import com.carocart.productservice.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminClient adminClient;

    private boolean isAdmin(AdminResponseDTO admin) {
        return admin != null && "ADMIN".equals(admin.getRole());
    }

    private void validateAdmin(String token) {
        AdminResponseDTO admin = adminClient.getCurrentAdmin(token);
        if (!isAdmin(admin)) {
            throw new RuntimeException("Unauthorized: Admin role required");
        }
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
    public void deleteProduct(Long id, String token) {
        validateAdmin(token);
        productRepository.deleteById(id);
    }

    @Override
    public Product addProduct(Product product, String token) {
        validateAdmin(token);

        if (product.getSubCategory() == null) {
            throw new RuntimeException("SubCategory must be provided");
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct, String token) {
        validateAdmin(token);

        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setDescription(updatedProduct.getDescription());
            product.setBrand(updatedProduct.getBrand());
            product.setPrice(updatedProduct.getPrice());
            product.setMrp(updatedProduct.getMrp());
            product.setDiscount(updatedProduct.getDiscount());
            product.setStock(updatedProduct.getStock());
            product.setUnit(updatedProduct.getUnit());
            product.setIsAvailable(updatedProduct.getIsAvailable());
            product.setUpdatedAt(LocalDateTime.now());
            product.setSubCategory(updatedProduct.getSubCategory());

            if (updatedProduct.getImage() != null && updatedProduct.getImage().length > 0) {
                product.setImage(updatedProduct.getImage());
            }
            return productRepository.save(product);
        }).orElse(null);
    }

    @Override
    public List<Product> getProductsBySubCategory(Long subCategoryId) {
        return productRepository.findBySubCategoryId(subCategoryId);
    }
    
    @Override
    public ProductDTO getProductDTOById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        // Generate image URL or path
        String imageUrl = "/api/images/" + product.getId(); // You can change this as per your image serving strategy

        // Map to ProductDTO including stock and availability
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setImageUrl(imageUrl);
        productDTO.setStock(product.getStock());
        productDTO.setIsAvailable(Boolean.TRUE.equals(product.getIsAvailable()));

        return productDTO;
    }
}
