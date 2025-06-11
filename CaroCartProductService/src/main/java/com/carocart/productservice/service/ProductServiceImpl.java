package com.carocart.productservice.service;

import com.carocart.productservice.dto.AdminResponseDTO;
import com.carocart.productservice.dto.ProductDTO;
import com.carocart.productservice.dto.VendorDTO;
import com.carocart.productservice.entity.Product;
import com.carocart.productservice.feign.AdminClient;
import com.carocart.productservice.feign.VendorClient;
import com.carocart.productservice.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AdminClient adminClient;

    @Autowired
    private VendorClient vendorClient;

    private boolean isAdmin(AdminResponseDTO admin) {
        return admin != null && "ADMIN".equals(admin.getRole());
    }

    private boolean isVendor(VendorDTO vendor) {
        return vendor != null && "VENDOR".equals(vendor.getRole());
    }

    private AdminResponseDTO tryGetAdmin(String token) {
        try {
            return adminClient.getCurrentAdmin(token);
        } catch (Exception e) {
            return null;
        }
    }

    private VendorDTO tryGetVendor(String token) {
        try {
            return vendorClient.getCurrentVendor(token);
        } catch (Exception e) {
            return null;
        }
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
        AdminResponseDTO admin = tryGetAdmin(token);
        VendorDTO vendor = tryGetVendor(token);

        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean isOwner = isAdmin(admin)
            || (isVendor(vendor) && "VENDOR".equals(product.getAddedByRole())
            && vendor.getId().equals(product.getAddedById()));

        if (!isOwner) {
            throw new RuntimeException("Unauthorized: Not allowed to delete this product");
        }

        productRepository.deleteById(id);
    }

    @Override
    public Product addProduct(Product product, String token) {
        AdminResponseDTO admin = tryGetAdmin(token);
        VendorDTO vendor = tryGetVendor(token);

        if (isAdmin(admin)) {
            product.setAddedByRole("ADMIN");
            product.setAddedById(admin.getId());
            product.setVendorName(null); // Not needed
        } else if (isVendor(vendor)) {
            product.setAddedByRole("VENDOR");
            product.setAddedById(vendor.getId());
            product.setVendorName(vendor.getFullName());
        } else {
            throw new RuntimeException("Unauthorized: Only admin or vendor can add products");
        }

        if (product.getSubCategory() == null) {
            throw new RuntimeException("SubCategory must be provided");
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct, String token) {
        AdminResponseDTO admin = tryGetAdmin(token);
        VendorDTO vendor = tryGetVendor(token);

        return productRepository.findById(id).map(product -> {
            boolean isOwner = isAdmin(admin)
                || (isVendor(vendor) && "VENDOR".equals(product.getAddedByRole())
                && vendor.getId().equals(product.getAddedById()));

            if (!isOwner) {
                throw new RuntimeException("Unauthorized: Not allowed to update this product");
            }

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

        String imageUrl = "/products/image/" + product.getId();

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(imageUrl);
        dto.setStock(product.getStock());
        dto.setIsAvailable(Boolean.TRUE.equals(product.getIsAvailable()));

        return dto;
    }

    @Override
    public List<Product> getProductsByVendor(String token) {
        VendorDTO vendor = tryGetVendor(token);
        if (!isVendor(vendor)) {
            throw new RuntimeException("Unauthorized: Only vendors can access this.");
        }

        return productRepository.findByAddedByRoleAndAddedById("VENDOR", vendor.getId());
    }
    
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

}
