package com.crypto212.auth.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException{
    public UserAlreadyExistsException(HttpStatus status, String message) {
        super(status, message);
    }
}
