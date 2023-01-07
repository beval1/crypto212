package com.crypto212.auth.service.dto.payload;


import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SigninDTO {
    @NotBlank
    private String usernameOrEmail;
    @NotBlank
    private String password;
}
