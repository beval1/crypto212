package com.crypto212.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private String message;
    private Object content;
    @Builder.Default
    private Instant timestamp = Instant.now();
}
