package com.crypto212.auth.repository.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleEntity {
    private long id;
    private RoleEnum roleName;
}
