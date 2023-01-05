package com.crypto212.auth.repository.postgres;

import com.crypto212.auth.repository.RoleRepository;
import com.crypto212.auth.repository.UserRepository;
import com.crypto212.auth.repository.entity.RoleEntity;
import com.crypto212.auth.repository.entity.UserEntity;
import com.crypto212.clients.idgenerator.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public UserEntity createUser(UserEntity userEntity) {
        return txTemplate.execute(status -> {
            long snowFlakeId = snowFlake.nextId();
            userEntity.setId(snowFlakeId);
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                        Queries.INSERT_USER);
                ps.setLong(1, userEntity.getId());
                ps.setString(2, userEntity.getUsername());
                ps.setString(3, userEntity.getEmail());
                ps.setString(4, userEntity.getPassword());
                ps.setBoolean(5, userEntity.isEnabled());
                ps.setBoolean(6, userEntity.isLocked());
                ps.setBoolean(7, userEntity.isAccountExpired());
                ps.setBoolean(8, userEntity.isCredentialsExpired());
                return ps;
            });

            for (RoleEntity role : userEntity.getRoles()) {
                jdbc.update(conn -> {
                    PreparedStatement ps = conn.prepareStatement(
                            Queries.INSERT_USER_ROLE);
                    ps.setLong(1, snowFlakeId);
                    ps.setLong(2, role.getId());
                    return ps;
                });
            }

            return userEntity;
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
        public static final String GET_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
        public static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
        public static final String INSERT_USER = "INSERT INTO users(id, username, email, pass, account_enabled, " +
                "account_locked, account_expired, credentials_expired) VALUES (?,?,?,?,?,?,?,?)";
        public static final String INSERT_USER_ROLE = "INSERT INTO users_roles(user_id, role_id) VALUES (?,?)";
        public static final String GET_USER_ROLES = "SELECT r.role_name, r.id FROM ROLES as r " +
                "JOIN USERS_ROLES as ur on r.id=ur.role_id WHERE ur.user_id=?";
    }

}
