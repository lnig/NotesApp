package com.example.demoapp.Data;

import com.example.demoapp.Validators.UserValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@UserValidation
public class User {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Integer age;

    public User(String firstName, String lastName, String username, String password, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.age = age;
    }
}
