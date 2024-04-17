package com.example.demoapp.Data.db;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categoriesrest", schema = "project_database")
@Data
public class CategoriesrestEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "categoryNameID", nullable = false)
    private int categoryNameId;
    @Basic
    @Column(name = "categoryName", nullable = false, length = 255)
    private String categoryName;
}
