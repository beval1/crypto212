package com.crypto212.auth.exception;

import com.crypto212.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException(HttpStatus status, String message) {
        super(status, message);
    }
}
