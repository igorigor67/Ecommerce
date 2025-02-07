package com.example.demo.exception.user;

import com.example.demo.exception.BaseException;

public class UserNotLoggedInException extends BaseException {
    public UserNotLoggedInException(String message) {
        super(message);
    }
}
