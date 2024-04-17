package com.example.demoapp.Validators;

import com.example.demoapp.Data.Login;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<LoginValidation, Login> {

    private static final String USERNAME_PATTERN = "^[a-z0-9]+$";

    @Override
    public boolean isValid(Login login, ConstraintValidatorContext cxt) {
        String username = login.getUsername();
        String password = login.getPassword();

        boolean isUsernameValid_SpaceCharacter = !username.contains(" ");
        boolean isUsernameValid_Size = username.length() >= 3 && username.length() <= 20;
        boolean isUsernameValid_Pattern = username.matches(USERNAME_PATTERN);


        boolean isPasswordValid_SpaceCharacter = !password.contains(" ");
        boolean isPasswordValid_Size = password.length() >= 5;

        if (!isUsernameValid_SpaceCharacter) {
            cxt.buildConstraintViolationWithTemplate("{err.string.LoginUsernameNoSpaceCharacter}")
                    .addPropertyNode("username")
                    .addConstraintViolation();
        }

        if (!isUsernameValid_Size) {
            cxt.buildConstraintViolationWithTemplate("{err.string.LoginUsernameLength}")
                    .addPropertyNode("username")
                    .addConstraintViolation();
        }

        if (!isUsernameValid_Pattern) {
            cxt.buildConstraintViolationWithTemplate("{err.string.LoginUsernameLowercaseOnly}")
                    .addPropertyNode("username")
                    .addConstraintViolation();
        }

        if (!isPasswordValid_SpaceCharacter) {
            cxt.buildConstraintViolationWithTemplate("{err.string.LoginPasswordNoSpaceCharacter}")
                    .addPropertyNode("password")
                    .addConstraintViolation();
        }

        if (!isPasswordValid_Size) {
            cxt.buildConstraintViolationWithTemplate("{err.string.LoginPasswordLength}")
                    .addPropertyNode("password")
                    .addConstraintViolation();
        }

        return isUsernameValid_SpaceCharacter && isUsernameValid_Size && isUsernameValid_Pattern && isPasswordValid_SpaceCharacter && isPasswordValid_Size;
    }
}