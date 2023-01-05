package com.crypto212.auth.repository.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private Set<RoleEntity> roles;
    private String password;
    private String email;
    private LocalDate birthdate;
//    private final ImageEntity profileImage;

    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private boolean locked = false;
    @Builder.Default
    private boolean accountExpired = false;
    @Builder.Default
    private boolean credentialsExpired = false;
}
