package com.example.coursework.exception;

public class UserWithSameUsernameAlreadyExist extends RuntimeException {

    public UserWithSameUsernameAlreadyExist(String message) {
        super(message);
    }
}
