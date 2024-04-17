package com.example.demoapp.repositories.db;

import com.example.demoapp.Data.db.SharednotesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharednotesEntityRepository extends JpaRepository<SharednotesEntity, Integer> {
    List<SharednotesEntity> findByUserId(Integer userId);
}