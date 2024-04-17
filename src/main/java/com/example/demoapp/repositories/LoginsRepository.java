package com.example.demoapp.repositories;

import com.example.demoapp.Data.Login;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
public class LoginsRepository {
    List<Login> loginList = new ArrayList<>();
}
