package com.example.demoapp.Services;

import com.example.demoapp.Data.Login;
import com.example.demoapp.repositories.LoginsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginsService {
    @Autowired
    LoginsRepository loginsRepository;

    public List<Login> getAllLogins() {
        List<Login> logins = loginsRepository.getLoginList();
        return logins;
    }

}
