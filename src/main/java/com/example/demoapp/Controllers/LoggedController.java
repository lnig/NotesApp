package com.example.demoapp.Controllers;

import com.example.demoapp.Data.db.AuthoritiesEntity;
import com.example.demoapp.Data.db.UsersEntity;
import com.example.demoapp.ModelAttributeClasses.MyInteger;
import com.example.demoapp.Services.UsersService;
import com.example.demoapp.repositories.AuthorityUsersRepository;
import com.example.demoapp.repositories.db.AuthoritiesEntityRepository;
import com.example.demoapp.repositories.db.UsersEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Logged")
public class LoggedController {

    @Autowired
    UsersService usersService;
    @Autowired
    AuthoritiesEntityRepository authoritiesEntityRepository;
    @Autowired
    UsersEntityRepository usersEntityRepository;
    @Autowired
    AuthorityUsersRepository authorityUsersRepository;

    @GetMapping("/MainMenu")
    public String MainMenu(){
        return "logged";
    }

    @GetMapping("/AdminPanel")
    public String AdminPanel(Model model) {
        List<UsersEntity> users = usersService.getAllUsersRole();
        model.addAttribute("users", users);
        return "adminPanel";
    }

    @GetMapping("/AdminPanel/User/{userId}")
    public String AdminPanelUser(@PathVariable("userId") int userId, Model model) {
        MyInteger userId2 = new MyInteger();
        userId2.setValue(userId);
        UsersEntity user = usersService.getUserById(userId2.getValue());
        model.addAttribute("user", user);
        model.addAttribute("authorities", authoritiesEntityRepository.findAllAuthorities());
        return "changeRole";
    }

    @PostMapping("/AdminPanel/change-role")
    public String changeUserRole(@RequestParam("userId") Integer userId, @RequestParam("role") int authorityId) {
        AuthoritiesEntity authority = authoritiesEntityRepository.findByAuthorityId(authorityId);

        UsersEntity usersEntity = usersService.createUserEntity(usersEntityRepository.findByUserId(userId));
        usersEntity.setAuthoritiesByAuthorityId(authority);
        usersEntity.setAuthorityId(authority.getAuthorityId());

        List<UsersEntity> users = authorityUsersRepository.getUsers();
        users.add(usersEntity);
        authorityUsersRepository.setUsers(users);
        return "redirect:/Logged/AdminPanel";
    }
}
