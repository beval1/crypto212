package com.crypto212.shared.exception;

import com.crypto212.shared.dto.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ObjectMapper objectMapper;
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
    public ResponseEntity<Object> handleFeign(FeignException ex) throws JsonProcessingException {
        ResponseDTO errorDto = objectMapper.readValue(ex.contentUTF8(), ResponseDTO.class);
        return ResponseEntity.status(ex.status())
                .body(
                        ResponseDTO
                                .builder()
                                .message(errorDto.getMessage())
                                .content(null)
                                .build()
                );
    }
}
