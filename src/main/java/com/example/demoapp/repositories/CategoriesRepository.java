package com.example.demoapp.repositories;

import com.example.demoapp.Data.db.CategoriesEntity;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
public class CategoriesRepository {
    List<CategoriesEntity> categoryList = new ArrayList<>();
    List<CategoriesEntity> deletedCategoryList = new ArrayList<>();

    public CategoriesEntity findCategoryById(List<CategoriesEntity> categoryList, int categoryId) {
        for (CategoriesEntity category : categoryList) {
            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }
        return null;
    }
    public CategoriesEntity findCategoryByCategoryName(String CategoryName) {
        for (CategoriesEntity category : categoryList) {
            if (category.getCategoryName().equals(CategoryName)) {
                return category;
            }
        }
        return null;
    }

}
