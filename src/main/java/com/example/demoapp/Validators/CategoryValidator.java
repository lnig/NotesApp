package com.example.demoapp.Validators;

import com.example.demoapp.Data.Category;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<CategoryValidation, Category> {

    @Override
    public boolean isValid(Category category, ConstraintValidatorContext cxt) {
        String categoryName = category.getCategoryName();

        boolean isCategoryNameValid_SpaceCharacter = !categoryName.contains(" ");
        boolean isCategoryNameValid_Size = categoryName.length() >= 3;


        if (!isCategoryNameValid_SpaceCharacter) {
            cxt.buildConstraintViolationWithTemplate("{err.string.CategoryNameNoSpaceCharacter}")
                    .addPropertyNode("categoryName")
                    .addConstraintViolation();
        }

        if (!isCategoryNameValid_Size) {
            cxt.buildConstraintViolationWithTemplate("{err.string.CategoryNameLength}")
                    .addPropertyNode("categoryName")
                    .addConstraintViolation();
        }

        return isCategoryNameValid_Size && isCategoryNameValid_SpaceCharacter;
    }
}
