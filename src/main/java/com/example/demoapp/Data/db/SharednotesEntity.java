package com.example.demoapp.Data.db;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sharednotes", schema = "project_database", catalog = "")
public class SharednotesEntity {
    @Id
    @Column(name = "noteID", nullable = false)
    private int noteId;
    @Basic
    @Column(name = "userID", nullable = false, insertable=false, updatable=false)
    private int userId;
    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID", nullable = false)
    private UsersEntity usersByUserId;


}
