package com.carocart.productservice.service;

import com.carocart.productservice.entity.Category;
import com.carocart.productservice.entity.SubCategory;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category, String token);
    SubCategory addSubCategory(SubCategory subCategory, String token);
    List<Category> getAllCategories();
    List<SubCategory> getSubCategoriesByCategoryId(Long categoryId);
    
    Category getCategoryById(Long id);

}
