package com.example.demo.exception.user;

import com.example.demo.exception.BaseException;

public class UsernameAlreadyInUseException extends BaseException {
    public UsernameAlreadyInUseException(String message) {
        super(message);
    }
}
