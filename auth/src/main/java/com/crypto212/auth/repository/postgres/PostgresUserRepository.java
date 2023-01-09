package com.crypto212.auth.repository.postgres;

import com.crypto212.auth.model.entity.RoleEntity;
import com.crypto212.auth.model.entity.UserEntity;
import com.crypto212.auth.repository.RoleRepository;
import com.crypto212.auth.repository.UserRepository;
import com.crypto212.idgenerator.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PostgresUserRepository implements UserRepository {
    private final TransactionTemplate txTemplate;
    private final JdbcTemplate jdbc;
    private final SnowFlake snowFlake;
    private final RoleRepository roleRepository;

    public PostgresUserRepository(TransactionTemplate txTemplate, JdbcTemplate jdbc, SnowFlake snowFlake, RoleRepository roleRepository) {
        this.txTemplate = txTemplate;
        this.jdbc = jdbc;
        this.snowFlake = snowFlake;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        List<UserEntity> userEntities = jdbc.query(Queries.GET_USER_BY_USERNAME,
                (rs, rowNum) -> userFromResultSet(rs), username);
        return !userEntities.isEmpty() ? Optional.of(userEntities.get(0)) : Optional.empty();
    }

    @Override
    public Optional<UserEntity> getUserByEmail(String email) {
        List<UserEntity> userEntities = jdbc.query(Queries.GET_USER_BY_EMAIL,
                (rs, rowNum) -> userFromResultSet(rs), email);
        return !userEntities.isEmpty() ? Optional.of(userEntities.get(0)) : Optional.empty();
    }

    @Override
    public UserEntity createUser(String username, String email, String password, boolean enabled, boolean locked,
                                 boolean accountExpired, boolean credentialsExpired, Set<RoleEntity> roles) {
        return txTemplate.execute(status -> {
            long snowFlakeId = snowFlake.nextId();
            Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        Queries.INSERT_USER);
                ps.setLong(1, snowFlakeId);
                ps.setString(2, username);
                ps.setString(3, email);
                ps.setString(4, password);
                ps.setBoolean(5, enabled);
                ps.setBoolean(6, locked);
                ps.setBoolean(7, accountExpired);
                ps.setBoolean(8, credentialsExpired);
                ps.setTimestamp(9, currentTime);
                ps.setTimestamp(10, currentTime);
                return ps;
            });

            for (RoleEntity role : roles) {
                jdbc.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            Queries.INSERT_USER_ROLE);
                    ps.setLong(1, snowFlakeId);
                    ps.setLong(2, role.getId());
                    return ps;
                });
            }

            return UserEntity.builder()
                    .id(snowFlakeId)
                    .username(username)
                    .email(email)
                    .password(password)
                    .enabled(enabled)
                    .locked(locked)
                    .accountExpired(accountExpired)
                    .credentialsExpired(credentialsExpired)
                    .createdAt(currentTime.toLocalDateTime())
                    .updatedAt(currentTime.toLocalDateTime())
                    .build();
        });
    }

    private UserEntity userFromResultSet(ResultSet rs) throws SQLException {
        return UserEntity.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .password(rs.getString("pass"))
                .email(rs.getString("email"))
                .enabled(rs.getBoolean("account_enabled"))
                .locked(rs.getBoolean("account_locked"))
                .accountExpired(rs.getBoolean("account_expired"))
                .credentialsExpired(rs.getBoolean("credentials_expired"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }

    @Override
    public Set<RoleEntity> getUserRoles(long userId) {
        List<RoleEntity> roleEntities = jdbc.query(Queries.GET_USER_ROLES,
                (rs, rowNum) -> roleRepository.roleFromResultSet(rs), userId);
        return new HashSet<>(roleEntities);
    }

    static class Queries {
        private Queries() {}
        public static final String GET_USER_BY_USERNAME = "SELECT id, username, pass, email, account_enabled, " +
                "account_locked, account_expired, credentials_expired, created_at, updated_at FROM users WHERE username = ?";
        public static final String GET_USER_BY_EMAIL = "SELECT id, username, pass, email, account_enabled, " +
                "account_locked, account_expired, credentials_expired, created_at, updated_at FROM users WHERE email = ?";
        public static final String INSERT_USER = "INSERT INTO users(id, username, email, pass, account_enabled, " +
                "account_locked, account_expired, credentials_expired, created_at, updated_at) VALUES (?,?,?,?,?,?,?,?,?,?)";
        public static final String INSERT_USER_ROLE = "INSERT INTO users_roles(user_id, role_id) VALUES (?,?)";
        public static final String GET_USER_ROLES = "SELECT r.role_name, r.id, r.created_at, r.updated_at FROM ROLES as r " +
                "JOIN USERS_ROLES as ur on r.id=ur.role_id WHERE ur.user_id=?";
    }

}
