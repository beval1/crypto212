package com.crypto212.auth.repository;

import com.crypto212.auth.model.entity.RoleEntity;
import com.crypto212.auth.model.entity.RoleEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public interface RoleRepository {
    Optional<RoleEntity> findByRoleName(RoleEnum roleEnum);
    RoleEntity createRole(RoleEnum roleEnum);
    RoleEntity roleFromResultSet(ResultSet rs) throws SQLException;
}
