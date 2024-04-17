package com.example.demoapp.Data.db;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "notes", schema = "project_database")
@Data
public class NotesEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "noteID", nullable = false)
    private int noteId;
    @Basic
    @Column(name = "userID", nullable = false, insertable=false, updatable=false)
    private int userId;
    @Basic
    @Column(name = "title", nullable = false, length = 20)
    private String title;
    @Basic
    @Column(name = "content", nullable = false, length = 500)
    private String content;
    @Basic
    @Column(name = "noteLink", nullable = false, length = 255)
    private String noteLink;
    @Basic
    @Column(name = "creationDate", nullable = false, length = 10)
    private String creationDate;
    @Basic
    @Column(name = "categoryID", nullable = false, insertable=false, updatable=false)
    private int categoryId;
    @ManyToOne
    @JoinColumn(name = "categoryID", referencedColumnName = "categoryID", nullable = false)
    private CategoriesEntity categoriesByCategoryId;
    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID", nullable = false)
    private UsersEntity usersByUserId;
}
