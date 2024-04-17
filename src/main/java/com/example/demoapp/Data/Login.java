package com.example.demoapp.Data;

import com.example.demoapp.Validators.LoginValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@LoginValidation
@AllArgsConstructor
public class Login implements Serializable {

    private String username;

    private String password;
}
