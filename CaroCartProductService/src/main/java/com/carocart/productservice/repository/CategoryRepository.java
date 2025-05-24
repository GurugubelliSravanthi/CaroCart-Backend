package com.carocart.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carocart.productservice.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}