// src/main/java/com/carocart/productservice/dto/SubCategoryDTO.java
package com.carocart.productservice.dto;

public class SubCategoryDTO {
    private String name;
    private Long categoryId;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
