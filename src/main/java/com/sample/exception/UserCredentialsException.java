package com.sample.exception;

public class UserCredentialsException extends RuntimeException {
    public UserCredentialsException(String message) {
        super(message);
    }

    public UserCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
