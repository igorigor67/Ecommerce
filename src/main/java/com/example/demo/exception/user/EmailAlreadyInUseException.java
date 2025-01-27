package com.example.demo.exception.user;

import com.example.demo.exception.BaseException;

public class EmailAlreadyInUseException extends BaseException {
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
