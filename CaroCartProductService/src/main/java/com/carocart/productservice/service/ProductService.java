package com.carocart.productservice.service;

import com.carocart.productservice.dto.ProductDTO;
import com.carocart.productservice.entity.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


public interface ProductService {
    Product addProduct(Product product, String token);
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product updateProduct(Long id, Product product, String token);
    void deleteProduct(Long id, String token);
    ProductDTO getProductDTOById(Long id);
    List<Product> getProductsBySubCategory(Long subCategoryId);
    List<Product> getProductsByVendor(String token);
    Page<Product> getAllProducts(Pageable pageable);

}
