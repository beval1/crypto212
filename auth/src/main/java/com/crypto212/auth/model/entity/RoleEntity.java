package com.crypto212.auth.model.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RoleEntity extends BaseEntity {
    private RoleEnum roleName;
}
