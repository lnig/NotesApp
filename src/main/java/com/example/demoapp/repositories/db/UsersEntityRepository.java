package com.example.demoapp.repositories.db;

import com.example.demoapp.Data.db.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsersEntityRepository extends JpaRepository<UsersEntity, Integer> {
    UsersEntity findByLogin(String login);
    UsersEntity findByUserId(Integer userId);
}