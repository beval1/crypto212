package com.crypto212.auth.repository.postgres;

import com.crypto212.auth.model.entity.RoleEntity;
import com.crypto212.auth.model.entity.RoleEnum;
import com.crypto212.auth.repository.RoleRepository;
import com.crypto212.idgenerator.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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
        List<RoleEntity> roleEntities = jdbc.query(Queries.GET_ROLE,
                (rs, rowNum) -> roleFromResultSet(rs), roleEnum.name());
        return !roleEntities.isEmpty() ? Optional.of(roleEntities.get(0)) : Optional.empty();
    }

    @Override
    public RoleEntity createRole(RoleEnum roleEnum) {
        long snowFlakeId = snowFlake.nextId();
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        Queries.INSERT_ROLE);
                ps.setLong(1, snowFlakeId);
                ps.setString(2, roleEnum.name());
                ps.setTimestamp(3, currentTime);
                ps.setTimestamp(4, currentTime);
                return ps;
            });

            return RoleEntity.builder().id(snowFlakeId).roleName(roleEnum).build();
    }

    public RoleEntity roleFromResultSet(ResultSet rs) throws SQLException {
        return RoleEntity
                .builder()
                .id(rs.getLong("id"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .roleName(RoleEnum.valueOf(rs.getString("role_name")))
                .build();
    }

    static class Queries {
        private Queries() {}
        public static final String GET_ROLE = "SELECT id, role_name, created_at, updated_at FROM roles WHERE role_name = ?";
        public static final String INSERT_ROLE = "INSERT INTO roles(id, role_name, created_at, updated_at)" +
                " VALUES (?, ?, ?, ?)";
    }
}
