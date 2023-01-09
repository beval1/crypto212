package com.crypto212.auth.web;

import com.crypto212.auth.model.dto.payload.SigninDTO;
import com.crypto212.auth.model.dto.payload.SignupDTO;
import com.crypto212.auth.model.dto.response.JwtResponseDTO;
import com.crypto212.auth.service.AuthService;
import com.crypto212.clients.auth.JwtTokenClaims;
import com.crypto212.shared.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDTO> signIn(@Valid @RequestBody SigninDTO signinDto) {

        String token = authService.signInUser(signinDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Signed in successfully!")
                                .content(JwtResponseDTO
                                        .builder()
                                        .accessToken(token)
                                        .tokenType("Bearer").build())
                                .build()
                );
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDTO> signUp(@Valid @RequestBody SignupDTO signupDto) {
        authService.signUpUser(signupDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Signed up successfully!")
                                .content(null)
                                .build()
                );
    }

    @GetMapping("/token-claims")
    public ResponseEntity<ResponseDTO> getTokenClaims(@RequestHeader("X-Auth-Token") String authToken) {
        JwtTokenClaims jwtTokenClaims = authService.getTokenClaims(authToken);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Token claims fetched successfully!")
                                .content(jwtTokenClaims)
                                .build()
                );
    }
}

