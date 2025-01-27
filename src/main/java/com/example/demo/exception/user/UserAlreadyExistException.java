package com.example.demo.exception.user;

import com.example.demo.exception.BaseException;

public class UserAlreadyExistException extends BaseException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
