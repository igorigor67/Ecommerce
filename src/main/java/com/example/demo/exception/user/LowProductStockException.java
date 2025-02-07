package com.example.demo.exception.user;

import com.example.demo.exception.BaseException;

public class LowProductStockException extends BaseException {
    public LowProductStockException(String message) {
        super(message);
    }
}
