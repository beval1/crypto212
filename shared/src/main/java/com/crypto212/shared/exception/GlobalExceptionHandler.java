package com.crypto212.shared.exception;

import com.crypto212.shared.dto.ResponseDTO;
import feign.FeignException;
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

    @ExceptionHandler({FeignException.class})
    public ResponseEntity<Object> handleFeign(FeignException ex) {
        return ResponseEntity.status(ex.status())
                .body(
                        ResponseDTO
                                .builder()
                                .message(ex.contentUTF8())
                                .content(null)
                                .build()
                );
    }
}
