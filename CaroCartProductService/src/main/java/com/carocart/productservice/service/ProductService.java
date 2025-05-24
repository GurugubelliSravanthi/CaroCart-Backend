package com.carocart.productservice.service;

import com.carocart.productservice.entity.Product;

import java.util.List;

public interface ProductService {
    Product addProduct(Product product, String token);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product updateProduct(Long id, Product product, String token);
    void deleteProduct(Long id, String token);

    List<Product> getProductsBySubCategory(Long subCategoryId);
}
