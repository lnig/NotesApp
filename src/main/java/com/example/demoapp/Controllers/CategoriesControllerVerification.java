package com.example.demoapp.Controllers;

import com.example.demoapp.Services.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demoapp.Connectors.RestCategoryConnector;

@RestController
@RequestMapping("/Logged/Categories/AddCategory/check/")
public class CategoriesControllerVerification {

    @Autowired
    CategoriesService categoriesService;

    @GetMapping("/{categoryName}")
    public ResponseEntity<Void> verifyCategoryExistence(@PathVariable String categoryName) {
        boolean categoryExists = categoriesService.checkCategoryExistence(categoryName);
        if (categoryExists) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}