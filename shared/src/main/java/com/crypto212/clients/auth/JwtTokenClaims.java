package com.crypto212.clients.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtTokenClaims {
    String roles;
    String userId;
}
