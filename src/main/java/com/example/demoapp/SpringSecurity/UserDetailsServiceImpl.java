package com.example.demoapp.SpringSecurity;

import com.example.demoapp.Data.Login;
import com.example.demoapp.Data.db.UsersEntity;
import com.example.demoapp.Services.LoginsService;
import com.example.demoapp.repositories.db.AuthoritiesEntityRepository;
import com.example.demoapp.repositories.db.UsersEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsersEntityRepository usersEntityRepository;
    @Autowired
    AuthoritiesEntityRepository authoritiesEntityRepository;

    @Autowired
    LoginsService loginsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity user = usersEntityRepository.findByLogin(username);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String password = request.getParameter("password");
        loginsService.getAllLogins().clear();
        loginsService.getAllLogins().add(new Login(username, password));

        if (user == null)
            throw new UsernameNotFoundException("User " + username + " not found !");
        else {
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getLogin(),
                    user.getPass(),
                    authoritiesEntityRepository.findAuthoritiesEntitiesByAuthorityId(user.getAuthorityId())
                            .stream()
                            .map(role -> {
                                return new SimpleGrantedAuthority(role.getAuthorityName());
                            })
                            .collect(Collectors.toSet()));

            HttpSession session = request.getSession(false);
            if (session != null && !session.isNew()) {
                session.setAttribute("UsersEntity", user);
            }
            return userDetails;
        }
    }
}