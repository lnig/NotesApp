package com.example.demoapp.Validators;

import com.example.demoapp.Data.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserValidator implements ConstraintValidator<UserValidation, User> {

    private static final String NAME_PATTERN = "\\p{Lu}\\p{L}*\\b";
    private static final String USERNAME_PATTERN = "^[a-z]+$";

    @Override
    public boolean isValid(User user, ConstraintValidatorContext cxt) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String username = user.getUsername();
        String password = user.getPassword();
        Integer age = user.getAge();

        boolean isFirstNameValid_SpaceCharacter = !firstName.contains(" ");
        boolean isFirstNameValid_Size = firstName.length() >= 3 && firstName.length() <= 20;
        boolean isFirstNameValid_Pattern = firstName.matches(NAME_PATTERN);

        boolean isLastNameValid_SpaceCharacter = !lastName.contains(" ");
        boolean isLastNameValid_Size = lastName.length() >= 3 && lastName.length() <= 20;
        boolean isLastNameValid_Pattern = lastName.matches(NAME_PATTERN);

        boolean isUsernameValid_SpaceCharacter = !username.contains(" ");
        boolean isUsernameValid_Size = username.length() >= 3 && username.length() <= 20;
        boolean isUsernameValid_Pattern = username.matches(USERNAME_PATTERN);

        boolean isPasswordValid_SpaceCharacter = !password.contains(" ");
        boolean isPasswordValid_Size = password.length() >= 5;

        boolean isAgeValid_Value = age >= 18;

        if (!isFirstNameValid_SpaceCharacter) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserFirstNoSpaceCharacter}")
                    .addPropertyNode("firstName")
                    .addConstraintViolation();
        }

        if (!isLastNameValid_SpaceCharacter) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserLastNoSpaceCharacter}")
                    .addPropertyNode("lastName")
                    .addConstraintViolation();
        }

        if (!isUsernameValid_SpaceCharacter) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserUsernameNoSpaceCharacter}")
                    .addPropertyNode("username")
                    .addConstraintViolation();
        }

        if (!isPasswordValid_SpaceCharacter) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserPasswordNoSpaceCharacter}")
                    .addPropertyNode("password")
                    .addConstraintViolation();
        }

        if (!isFirstNameValid_Size) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserFirstNameLength}")
                    .addPropertyNode("firstName")
                    .addConstraintViolation();
        }

        if (!isLastNameValid_Size) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserLastNameLength}")
                    .addPropertyNode("lastName")
                    .addConstraintViolation();
        }

        if (!isUsernameValid_Size) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserUsernameLength}")
                    .addPropertyNode("username")
                    .addConstraintViolation();
        }

        if (!isPasswordValid_Size) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserPasswordLength}")
                    .addPropertyNode("password")
                    .addConstraintViolation();
        }

        if (!isAgeValid_Value) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserMinimumAge}")
                    .addPropertyNode("age")
                    .addConstraintViolation();
        }

        if (!isFirstNameValid_Pattern) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserFirstNameStartsWithUppercase}")
                    .addPropertyNode("firstName")
                    .addConstraintViolation();
        }

        if (!isLastNameValid_Pattern) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserLastNameStartsWithUppercase}")
                    .addPropertyNode("lastName")
                    .addConstraintViolation();
        }

        if (!isUsernameValid_Pattern) {
            cxt.buildConstraintViolationWithTemplate("{err.string.UserUsernameLowercaseOnly}")
                    .addPropertyNode("username")
                    .addConstraintViolation();
        }

        return isFirstNameValid_SpaceCharacter && isFirstNameValid_Size && isLastNameValid_SpaceCharacter && isLastNameValid_Size
                && isUsernameValid_SpaceCharacter && isUsernameValid_Size && isPasswordValid_SpaceCharacter && isPasswordValid_Size
                && isAgeValid_Value && isFirstNameValid_Pattern && isLastNameValid_Pattern && isUsernameValid_Pattern;
    }
}
