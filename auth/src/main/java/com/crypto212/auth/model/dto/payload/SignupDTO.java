package com.crypto212.auth.model.dto.payload;

import com.crypto212.auth.util.validators.EmailValidator;
import com.crypto212.auth.util.validators.UserUsernameValidator;
import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SignupDTO {
    @UserUsernameValidator
    @NotBlank
    private String username;
    @EmailValidator
    private String email;
    @NotBlank
    private String password;
}
