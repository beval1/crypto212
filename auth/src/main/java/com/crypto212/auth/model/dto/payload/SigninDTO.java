package com.crypto212.auth.model.dto.payload;


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
