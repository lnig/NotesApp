package com.example.demoapp.repositories.db;

import com.example.demoapp.Data.db.CategoriesrestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesrestEntityRepository extends JpaRepository<CategoriesrestEntity, Integer> {
    CategoriesrestEntity findByCategoryName(String categoryName);
}