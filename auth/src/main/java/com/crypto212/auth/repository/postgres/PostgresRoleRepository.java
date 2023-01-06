package com.crypto212.auth.repository.postgres;

import com.crypto212.auth.repository.RoleRepository;
import com.crypto212.auth.repository.entity.RoleEntity;
import com.crypto212.auth.repository.entity.RoleEnum;
import com.crypto212.idgenerator.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class PostgresRoleRepository implements RoleRepository {
    private final JdbcTemplate jdbc;
    private final SnowFlake snowFlake;

    public PostgresRoleRepository(JdbcTemplate jdbc, SnowFlake snowFlake) {
        this.jdbc = jdbc;
        this.snowFlake = snowFlake;
    }

    @Override
    public Optional<RoleEntity> findByRoleName(RoleEnum roleEnum) {
        return Optional.ofNullable(jdbc.queryForObject(Queries.GET_ROLE,
                (rs, rowNum) -> roleFromResultSet(rs), roleEnum.name()));
    }

    @Override
    public RoleEntity createRole(RoleEnum roleEnum) {
        long snowFlakeId = snowFlake.nextId();
        jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        Queries.INSERT_ROLE);
                ps.setLong(1, snowFlakeId);
                ps.setString(2, roleEnum.name());
                return ps;
            });

            return RoleEntity.builder().id(snowFlakeId).roleName(roleEnum).build();
    }

    public RoleEntity roleFromResultSet(ResultSet rs) throws SQLException {
        return RoleEntity
                .builder()
                .id(rs.getLong("id"))
                .roleName(RoleEnum.valueOf(rs.getString("role_name")))
                .build();
    }

    static class Queries {
        private Queries() {}
        public static final String GET_ROLE = "SELECT id, role_name FROM roles WHERE role_name = ?";
        public static final String INSERT_ROLE = "INSERT INTO roles(id, role_name) VALUES (?, ?)";
    }
}
