package com.example.demoapp.Data;

import com.example.demoapp.Validators.CategoryValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CategoryValidation
public class Category {
    String categoryName;
}
