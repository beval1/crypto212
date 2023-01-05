package com.crypto212.auth.repository;

import com.crypto212.auth.repository.entity.RoleEntity;
import com.crypto212.auth.repository.entity.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface UserRepository {
    UserEntity createUser(UserEntity userEntity);
    Set<RoleEntity> getUserRoles(long userId);
    Optional<UserEntity> getUserByUsername(String username);
    Optional<UserEntity> getUserByEmail(String email);
}
