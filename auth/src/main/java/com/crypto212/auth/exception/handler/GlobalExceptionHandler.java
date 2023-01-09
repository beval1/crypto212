package com.crypto212.auth.exception.handler;

import com.crypto212.shared.dto.ResponseDTO;
import com.crypto212.shared.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<Object> handleCustomExceptions(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(
                        ResponseDTO
                                .builder()
                                .message(ex.getMessage())
                                .content(null)
                                .build()
                );
    }
}
