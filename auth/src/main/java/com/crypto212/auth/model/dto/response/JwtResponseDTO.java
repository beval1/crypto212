package com.crypto212.auth.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponseDTO {
    private String accessToken;
    private String tokenType;
}
