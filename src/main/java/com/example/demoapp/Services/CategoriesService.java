package com.example.demoapp.Services;

import com.example.demoapp.Data.Category;
import com.example.demoapp.Data.db.CategoriesEntity;
import com.example.demoapp.Data.db.UsersEntity;
import com.example.demoapp.repositories.CategoriesRepository;
import com.example.demoapp.repositories.NotesRepository;
import com.example.demoapp.repositories.db.CategoriesEntityRepository;
import com.example.demoapp.repositories.db.CategoriesrestEntityRepository;
import com.example.demoapp.repositories.db.NotesEntityRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriesService {
    @Autowired
    CategoriesEntityRepository categoriesEntityRepository;

    @Autowired
    CategoriesRepository categoriesRepository;

    @Autowired
    CategoriesrestEntityRepository categoriesrestEntityRepository;

    @Autowired
    HttpSession session;

    @Autowired
    NotesEntityRepository notesEntityRepository;

    @Autowired
    NotesRepository notesRepository;

    public boolean addNewCategory(Category newCategory) {
        UsersEntity usersEntity = (UsersEntity) session.getAttribute("UsersEntity");
        if (categoriesEntityRepository.findByCategoryNameAndUserId(newCategory.getCategoryName(),usersEntity.getUserId()) != null) {
            return false;
        }
        CategoriesEntity categoriesEntity = convertToCategoryEntity(newCategory);
        categoriesRepository.getCategoryList().add(categoriesEntity);
        return true;
    }

    public List<CategoriesEntity> getAllCategoriesDB() {
        List<CategoriesEntity> categoriesRepo = categoriesRepository.getCategoryList();
        List<CategoriesEntity> categoriesDB = filterCategoriesByDeleted(categoriesEntityRepository.findAll());
        List<CategoriesEntity> allCategories = new ArrayList<>();
        allCategories.addAll(categoriesRepo);
        allCategories.addAll(categoriesDB);
        UsersEntity usersEntity = (UsersEntity) session.getAttribute("UsersEntity");
        allCategories = filterCategoriesByUserId(allCategories, usersEntity.getUserId());
        return allCategories;
    }

    public List<CategoriesEntity> filterCategoriesByDeleted(List<CategoriesEntity> categoryList) {
        categoryList = copyCategoriesEntityList(categoryList);
        List<CategoriesEntity> filteredCategories = new ArrayList<>(categoryList);
        if(categoriesRepository.getDeletedCategoryList() == null) return categoryList;
        List<CategoriesEntity> deletedCategoryList = categoriesRepository.getDeletedCategoryList();
        for (CategoriesEntity deletedCategory : deletedCategoryList) {
            int categoryIndex = deletedCategory.getCategoryId();
            boolean shouldRemove = false;
            for (CategoriesEntity category : categoryList) {
                if (category.getCategoryId() == categoryIndex) {
                    shouldRemove = true;
                    break;
                }
            }
            if (shouldRemove) {
                filteredCategories.removeIf(category -> category.getCategoryId() == categoryIndex);
            }
        }
        return filteredCategories;
    }

    public CategoriesEntity findCategoryByNameFiltered( String categoryName) {
        List<CategoriesEntity> categoryList = filterCategoriesByDeleted(categoriesEntityRepository.findAll());
        UsersEntity usersEntity = (UsersEntity) session.getAttribute("UsersEntity");
        categoryList = filterCategoriesByUserId(categoryList,usersEntity.getUserId());
        for (CategoriesEntity category : categoryList) {
            if (category.getCategoryName().equals(categoryName)) {
                return category;
            }
        }
        return null;
    }

    public List<CategoriesEntity> filterCategoriesByUserId(List<CategoriesEntity> categories, int userId) {
        List<CategoriesEntity> filteredCategories = new ArrayList<>();
        for (CategoriesEntity category : categories) {
            if (category.getUserId() == userId) {
                filteredCategories.add(category);
            }
        }
        return filteredCategories;
    }

    public List<String> combineBothRepositories() {
        List<CategoriesEntity> categoriesEntities = getAllCategoriesDB();
        List<String> categoryNames = new ArrayList<>();
        for (CategoriesEntity entity : categoriesEntities) {
            categoryNames.add(entity.getCategoryName());
        }
        return categoryNames;
    }

    public int findCategoryIdByName(String categoryName) {
        List<CategoriesEntity> categoriesEntities = getAllCategoriesDB();
        for (CategoriesEntity category : categoriesEntities) {
            if (category.getCategoryName().equals(categoryName)) {
                return category.getCategoryId();
            }
        }
        return -1;
    }
    public List<CategoriesEntity> setCategoryNameByDeletedCategoryId(int categoryId, String newName) {
        List<CategoriesEntity> categoriesEntities = categoriesRepository.getDeletedCategoryList();
        for (int i=0;i<categoriesEntities.size();i++) {
            if (categoriesEntities.get(i).getCategoryId() == categoryId) {
                categoriesEntities.get(i).setCategoryName(newName);

                return categoriesEntities;
            }
        }
        return null;
    }

    public CategoriesEntity findCategoryIdByName(List<CategoriesEntity> categoriesEntities, int categoryId) {
        for (CategoriesEntity category : categoriesEntities) {
            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }
        return null;
    }

    public CategoriesEntity findCategoryById(int categoryId) {
        List<CategoriesEntity> categoriesEntities = getAllCategoriesDB();
        for (CategoriesEntity category : categoriesEntities) {
            if (category.getCategoryId() == categoryId) {
                return category;
            }
        }
        return null;
    }

    public int getNextCategoriesId() {
        List<CategoriesEntity> categories = categoriesEntityRepository.findAll();
        List<CategoriesEntity> categoriesv2 = categoriesRepository.getCategoryList();

        categories.addAll(categoriesv2);

        int maxCategoryId = 0;

        for (CategoriesEntity category : categories) {
            int categoryId = category.getCategoryId();
            if (categoryId > maxCategoryId) {
                maxCategoryId = categoryId;
            }
        }

        return maxCategoryId  + 1;
    }

    public boolean checkCategoryExistence(String categoryName) {
        return categoriesrestEntityRepository.findByCategoryName(categoryName) != null;
    }
    public List<CategoriesEntity> copyCategoriesEntityList(List<CategoriesEntity> categoriesEntityList){
        List<CategoriesEntity> categoriesEntities = new ArrayList<>();

        for (int i = 0; i < categoriesEntityList.size(); ++i) {
            categoriesEntities.add(copyCategoriesEntity(categoriesEntityList.get(i)));
        }
        return categoriesEntities;
    }

    public CategoriesEntity convertToCategoryEntity(Category category) {
        UsersEntity usersEntity = (UsersEntity) session.getAttribute("UsersEntity");

        CategoriesEntity categoriesEntity = new CategoriesEntity();
        categoriesEntity.setCategoryId(getNextCategoriesId());
        categoriesEntity.setCategoryName(category.getCategoryName());
        categoriesEntity.setNotesByCategoryId(notesEntityRepository.findByCategoryId(categoriesEntity.getCategoryId()));
        categoriesEntity.setUserId(usersEntity.getUserId());
        categoriesEntity.setUsersByUserId(usersEntity);

        return categoriesEntity;
    }

    public List<CategoriesEntity> removeCategoryById(List<CategoriesEntity> categoryList, int categoryId) {
        categoryList.removeIf(category -> category.getCategoryId() == categoryId);
        return categoryList;
    }

    public boolean deleteCategory(int categoryId){
        if(deleteCategoryRepo(categoryId) || deleteCategoryDB(categoryId)){
            return true;
        }
        return false;
    }

    public static CategoriesEntity copyCategoriesEntity(CategoriesEntity source) {
        CategoriesEntity destination = new CategoriesEntity();
        destination.setCategoryId(source.getCategoryId());
        destination.setCategoryName(source.getCategoryName());
        destination.setUserId(source.getUserId());
        destination.setUsersByUserId(source.getUsersByUserId());
        destination.setNotesByCategoryId(source.getNotesByCategoryId());
        return destination;
    }
    public boolean deleteCategoryDB(int categoryId){
        List<CategoriesEntity> categories = getAllCategoriesDB();
        List<CategoriesEntity> deletedCategories = categoriesRepository.getDeletedCategoryList();
        for (CategoriesEntity category : categories) {
            if (category.getCategoryId() == categoryId) {
                if(notesEntityRepository.findByCategoryId(categoryId).isEmpty() && categoriesEntityRepository.findByCategoryId(categoryId) != null){
                    deletedCategories.add(category);
                    categoriesEntityRepository.delete(category);
                    return true;
                }
            }
        }
        return false;
    }
    public boolean deleteCategoryRepo(int categoryId){
        List<CategoriesEntity> categories = categoriesRepository.getCategoryList();
        for (CategoriesEntity category : categories) {
            if (category.getCategoryId() == categoryId) {
                if(notesRepository.getNoteByCategoryId(categoryId) == null && categoriesRepository.findCategoryById(categories,categoryId) != null){
                    categories = removeCategoryById(categories,categoryId);
                    categoriesRepository.setCategoryList(categories);
                    return true;
                }

            }
        }
        return false;
    }
    public boolean editCategoryRepo(int categoryId, String newCategoryName){
        List<CategoriesEntity> categories = categoriesRepository.getCategoryList();
        for (CategoriesEntity category : categories) {
            if (category.getCategoryId() == categoryId) {
                category.setCategoryName(newCategoryName);
            }
        }
        categoriesRepository.setCategoryList(categories);
        return false;
    }

    public void saveCategoriesToDB(){
        List<CategoriesEntity> categories = filterCategoriesByDeleted(categoriesEntityRepository.findAll());
        for (CategoriesEntity category : categories) {
            if (!categoriesEntityRepository.findByCategoryId(category.getCategoryId()).getCategoryName().equals(category.getCategoryName())) {
                categoriesEntityRepository.findByCategoryId(category.getCategoryId()).setCategoryName(category.getCategoryName());
            }
        }
        categories = categoriesRepository.getCategoryList();
        categoriesEntityRepository.saveAll(categories);
    }

    public void endCategoryRepo(){
        categoriesRepository.getCategoryList().clear();
        categoriesRepository.getDeletedCategoryList().clear();
    }
}
