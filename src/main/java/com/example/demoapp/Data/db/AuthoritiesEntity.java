package com.example.demoapp.Data.db;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "authorities", schema = "project_database")
@Data
public class AuthoritiesEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "authorityID", nullable = false)
    private int authorityId;
    @Basic
    @Column(name = "authorityName", nullable = false, length = 255)
    private String authorityName;
    @OneToMany(mappedBy = "authoritiesByAuthorityId")
    private Collection<UsersEntity> usersByAuthorityId;
}
