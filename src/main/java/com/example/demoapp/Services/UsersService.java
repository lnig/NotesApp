package com.example.demoapp.Services;

import com.example.demoapp.Data.User;
import com.example.demoapp.Data.db.UsersEntity;
import com.example.demoapp.repositories.AuthorityUsersRepository;
import com.example.demoapp.repositories.db.UsersEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    @Autowired
    UsersEntityRepository usersEntityRepository;

    @Autowired
    AuthorityUsersRepository authorityUsersRepository;

    @Autowired
    AuthorityService authorityService;

    public void addUserToDB(UsersEntity usersEntity){
        usersEntityRepository.save(usersEntity);
    }

    public List<UsersEntity> getAllUsersRole() {
        List<UsersEntity> users = usersEntityRepository.findAll();
        List<UsersEntity> authorityUsers = authorityUsersRepository.getUsers();

        for (UsersEntity authorityUser : authorityUsers) {
            int authorityUserId = authorityUser.getUserId();

            for (int i = 0; i < users.size(); i++) {
                UsersEntity user = users.get(i);
                int userId = user.getUserId();

                if (userId == authorityUserId) {
                    users.set(i, authorityUser);
                    break;
                }
            }
        }
        return users;
    }

    public UsersEntity getUserById(int userId) {
        List<UsersEntity> users = usersEntityRepository.findAll();
        for (UsersEntity user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    public UsersEntity createUserEntity(UsersEntity originalUserEntity) {
        UsersEntity newUserEntity = new UsersEntity();

        newUserEntity.setUserId(originalUserEntity.getUserId());
        newUserEntity.setAuthorityId(originalUserEntity.getAuthorityId());
        newUserEntity.setFirstName(originalUserEntity.getFirstName());
        newUserEntity.setLastName(originalUserEntity.getLastName());
        newUserEntity.setLogin(originalUserEntity.getLogin());
        newUserEntity.setPass(originalUserEntity.getPass());
        newUserEntity.setAge(originalUserEntity.getAge());
        newUserEntity.setCategoriesByUserId(originalUserEntity.getCategoriesByUserId());
        newUserEntity.setNotesByUserId(originalUserEntity.getNotesByUserId());
        newUserEntity.setAuthoritiesByAuthorityId(originalUserEntity.getAuthoritiesByAuthorityId());

        return newUserEntity;
    }

    public UsersEntity mapToUserEntity(User user) {
        UsersEntity userEntity = new UsersEntity();
        userEntity.setAuthoritiesByAuthorityId(authorityService.getAuthorityByUserID(2));
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setLogin(user.getUsername());
        userEntity.setPass(user.getPassword());
        userEntity.setAge(user.getAge());
        return userEntity;
    }

}
