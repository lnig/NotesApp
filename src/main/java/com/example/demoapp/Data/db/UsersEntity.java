package com.example.demoapp.Data.db;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "users", schema = "project_database")
@Data
public class UsersEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "userID", nullable = false)
    private int userId;
    @Basic
    @Column(name = "authorityID", nullable = false, insertable=false, updatable=false)
    private int authorityId;
    @Basic
    @Column(name = "firstName", nullable = false, length = 20)
    private String firstName;
    @Basic
    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;
    @Basic
    @Column(name = "login", nullable = false, length = 20)
    private String login;
    @Basic
    @Column(name = "pass", nullable = false, length = 255)
    private String pass;
    @Basic
    @Column(name = "age", nullable = false)
    private int age;
    @OneToMany(mappedBy = "usersByUserId")
    private Collection<CategoriesEntity> categoriesByUserId;
    @OneToMany(mappedBy = "usersByUserId")
    private Collection<NotesEntity> notesByUserId;
    @ManyToOne
    @JoinColumn(name = "authorityID", referencedColumnName = "authorityID", nullable = false)
    private AuthoritiesEntity authoritiesByAuthorityId;
    @OneToMany(mappedBy = "usersByUserId")
    private Collection<SharednotesEntity> sharednotesByUserId;

}
