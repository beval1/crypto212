package com.crypto212.clients.auth;

import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "auth",
        url = "${clients.auth.url}"
)
public interface AuthClient {

    @GetMapping(path = "api/v1/auth/token-claims")
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer {token}"
    })
    JwtTokenClaims getTokenClaims(@Param("token") String token);

}
