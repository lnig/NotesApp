package com.example.demoapp.Services;

import com.example.demoapp.Data.db.AuthoritiesEntity;

import com.example.demoapp.Data.db.UsersEntity;
import com.example.demoapp.repositories.AuthorityUsersRepository;
import com.example.demoapp.repositories.db.AuthoritiesEntityRepository;
import com.example.demoapp.repositories.db.UsersEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityService {
    @Autowired
    AuthoritiesEntityRepository authoritiesEntityRepository;

    @Autowired
    AuthorityUsersRepository authorityUsersRepository;

    @Autowired
    UsersEntityRepository usersEntityRepository;

    public void saveCategoriesToDB() {
        List<UsersEntity> usersEntityList = authorityUsersRepository.getUsers();
        for (UsersEntity user : usersEntityList) {
            UsersEntity fetchedUser = usersEntityRepository.findByUserId(user.getUserId());

            fetchedUser.setAuthorityId(user.getAuthorityId());
            fetchedUser.setAuthoritiesByAuthorityId(user.getAuthoritiesByAuthorityId());

            usersEntityRepository.save(fetchedUser);
        }
    }

    public void endAuthorityRepo() {
        authorityUsersRepository.setUsers(new ArrayList<>());
    }

    public AuthoritiesEntity getAuthorityByUserID(Integer userID){
        return authoritiesEntityRepository.findByAuthorityId(userID);
    }
}

