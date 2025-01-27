package com.example.demo.exception.user;

import com.example.demo.exception.BaseException;

public class ContactNumberAlreadyInUseException extends BaseException{
    public ContactNumberAlreadyInUseException(String message) {
        super(message);
    }
}
