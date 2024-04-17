package com.example.demoapp.repositories.db;

import com.example.demoapp.Data.db.CategoriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesEntityRepository extends JpaRepository<CategoriesEntity, Integer> {
    CategoriesEntity findByCategoryName(String categoryName);
    CategoriesEntity findByCategoryId(Integer categoryId);
    CategoriesEntity findByCategoryNameAndUserId(String categoryName,Integer userId);
}