package com.carocart.productservice.controller;

import com.carocart.productservice.dto.SubCategoryDTO;
import com.carocart.productservice.entity.Category;
import com.carocart.productservice.entity.SubCategory;
import com.carocart.productservice.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // ✅ Admin: Add category
    @PostMapping("/admin/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category,
                                                @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(categoryService.addCategory(category, token));
    }

    // ✅ Admin: Add subcategory
    @PostMapping("/admin/subcategory/add")
    public ResponseEntity<SubCategory> addSubCategory(@RequestBody SubCategoryDTO dto,
                                                      @RequestHeader("Authorization") String token) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(dto.getName());

        Category category = categoryService.getCategoryById(dto.getCategoryId());
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        subCategory.setCategory(category);

        SubCategory savedSubCategory = categoryService.addSubCategory(subCategory, token);
        return ResponseEntity.ok(savedSubCategory);
    }


    // ✅ User: Get all categories
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // ✅ User: Get subcategories by category
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<SubCategory>> getSubCategories(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getSubCategoriesByCategoryId(id));
    }
}
