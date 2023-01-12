package com.crypto212.clients.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth",
        url = "${clients.auth.url}"
)
public interface AuthClient {

    @GetMapping("api/v1/auth/token-claims")
    JwtTokenClaims getTokenClaims(@RequestHeader("X-Auth-Token") String token);

}
