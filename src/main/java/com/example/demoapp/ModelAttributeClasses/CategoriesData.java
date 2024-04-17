package com.example.demoapp.ModelAttributeClasses;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CategoriesData {
    private List<String> categoriesNames;
}
