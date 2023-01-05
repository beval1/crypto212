package com.crypto212.auth.web;

import com.crypto212.auth.service.AuthService;
import com.crypto212.auth.web.dto.payload.SigninDTO;
import com.crypto212.auth.web.dto.payload.SignupDTO;
import com.crypto212.auth.web.dto.response.JwtResponseDTO;
import com.crypto212.clients.shared.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}

