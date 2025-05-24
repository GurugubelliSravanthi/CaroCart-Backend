package com.carocart.productservice.service;

import com.carocart.productservice.dto.AdminResponseDTO;
import com.carocart.productservice.entity.Category;
import com.carocart.productservice.entity.SubCategory;
import com.carocart.productservice.feign.AdminClient;
import com.carocart.productservice.repository.CategoryRepository;
import com.carocart.productservice.repository.SubCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private SubCategoryRepository subCategoryRepo;

    @Autowired
    private AdminClient adminClient;

    private void validateAdmin(String token) {
        AdminResponseDTO admin = adminClient.getCurrentAdmin(token);
        if (admin == null || !"ADMIN".equals(admin.getRole())) {
            throw new RuntimeException("Unauthorized: Admin role required");
        }
    }

    @Override
    public Category addCategory(Category category, String token) {
        validateAdmin(token);
        return categoryRepo.save(category);
    }

    @Override
    public SubCategory addSubCategory(SubCategory subCategory, String token) {
        validateAdmin(token);
        return subCategoryRepo.save(subCategory);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public List<SubCategory> getSubCategoriesByCategoryId(Long categoryId) {
        return subCategoryRepo.findByCategoryId(categoryId);
    }
    
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

}
