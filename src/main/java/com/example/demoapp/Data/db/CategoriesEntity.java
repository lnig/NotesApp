package com.example.demoapp.Data.db;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Collection;

@Entity
@Table(name = "categories", schema = "project_database")
@Data
public class CategoriesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "categoryID", nullable = false)
    private int categoryId;
    @Basic
    @Column(name = "categoryName", nullable = false, length = 20)
    private String categoryName;
    @Basic
    @Column(name = "userID", nullable = false, insertable=false, updatable=false)
    private int userId;
    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID", nullable = false)
    private UsersEntity usersByUserId;
    @OneToMany(mappedBy = "categoriesByCategoryId")
    private Collection<NotesEntity> notesByCategoryId;
}
