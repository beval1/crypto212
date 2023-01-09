package com.crypto212.auth.exception;

import com.crypto212.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends ApiException {
    public RoleNotFoundException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Role not found!");
    }

    public RoleNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}