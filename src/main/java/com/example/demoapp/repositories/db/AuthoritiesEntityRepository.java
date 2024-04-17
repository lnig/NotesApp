package com.example.demoapp.repositories.db;

import com.example.demoapp.Data.db.AuthoritiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthoritiesEntityRepository extends JpaRepository<AuthoritiesEntity, Integer> {
    List<AuthoritiesEntity> findAuthoritiesEntitiesByAuthorityId(Integer authorityID);
    @Query("SELECT a FROM AuthoritiesEntity a")
    List<AuthoritiesEntity> findAllAuthorities();

    AuthoritiesEntity findByAuthorityId(Integer authorityId);
}