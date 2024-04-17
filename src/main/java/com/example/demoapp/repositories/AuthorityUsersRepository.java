package com.example.demoapp.repositories;

import com.example.demoapp.Data.db.UsersEntity;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
@Data
public class AuthorityUsersRepository {
    List<UsersEntity> Users = new LinkedList<>();
}
