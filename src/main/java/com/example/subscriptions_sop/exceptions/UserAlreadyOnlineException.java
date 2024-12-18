package com.example.subscriptions_sop.exceptions;

public class UserAlreadyOnlineException extends RuntimeException {
    public UserAlreadyOnlineException(String message) {
        super(message);
    }
}
