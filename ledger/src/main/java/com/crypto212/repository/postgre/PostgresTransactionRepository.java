package com.crypto212.repository.postgre;

import com.crypto212.idgenerator.SnowFlake;
import com.crypto212.repository.TransactionRepository;
import com.crypto212.repository.entity.TransactionEntity;
import com.crypto212.repository.entity.TransactionType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PostgresTransactionRepository implements TransactionRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SnowFlake snowFlake;

    public PostgresTransactionRepository(JdbcTemplate jdbcTemplate, SnowFlake snowFlake) {
        this.jdbcTemplate = jdbcTemplate;
        this.snowFlake = snowFlake;
    }

    @Override
    public List<TransactionEntity> getAllTransactionsForUser(Long userId) {
        return jdbcTemplate.query(Queries.GET_ALL_TRANSACTIONS_FOR_USER,
                (rs, rowNum) -> transactionEntityFromResultSet(rs), userId);
    }

    @Override
    public TransactionEntity createTransaction(Long userId, Long walletId, String type, String amount, String assetSymbol) {
        Long transactionTypeId = jdbcTemplate.query(Queries.GET_TRANSACTION_TYPE_ID,
                (rs, rowNum) -> {return rs.getLong("id");}, type).get(0);
        long snowFlakeId = snowFlake.nextId();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(Queries.INSERT_TRANSACTION);
            ps.setLong(1, snowFlakeId);
            ps.setLong(2, userId);
            ps.setLong(3, walletId);
            ps.setBigDecimal(4, new BigDecimal(amount));
            ps.setLong(5, transactionTypeId);
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        });
        return TransactionEntity.
                builder()
                .id(snowFlakeId)
                .walletId(walletId)
                .userId(userId)
                .amount(new BigDecimal(amount))
                .assetSymbol(assetSymbol)
                .transactionType(TransactionType.valueOf(type))
                .build();
    }

    @Override
    public List<TransactionType> getAllTransactionTypes() {
        return jdbcTemplate.query(Queries.GET_ALL_TRANSACTION_TYPES, (rs, rowNum) -> {
            return TransactionType.valueOf(rs.getString("name"));
        });
    }

    @Override
    public TransactionType createTransactionType(String name) {
        long snowFlakeId = snowFlake.nextId();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(Queries.INSERT_TRANSACTION_TYPE);
            ps.setLong(1, snowFlakeId);
            ps.setString(2, name);
            return ps;
        });
        return TransactionType.valueOf(name);
    }

    private TransactionEntity transactionEntityFromResultSet(ResultSet rs) throws SQLException {
        return TransactionEntity.
                builder()
                .id(rs.getLong("id"))
                .walletId(rs.getLong("wallet_id"))
                .userId(rs.getLong("user_id"))
                .amount(rs.getBigDecimal("transaction_amount"))
                .assetSymbol(rs.getString("asset_symbol"))
                .transactionType(TransactionType.valueOf(rs.getString("name")))
                .build();
    }

    public static class Queries {
        private Queries() {
        }

        public static final String INSERT_TRANSACTION_TYPE = "INSERT INTO transaction_type(id, name) VALUES (?, ?)";
        public static final String INSERT_TRANSACTION = "INSERT INTO transactions(id, user_id, wallet_id, " +
                "transaction_amount, transaction_type, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        public static final String GET_ALL_TRANSACTION_TYPES = "SELECT id, name FROM transaction_type";
        public static final String GET_TRANSACTION_TYPE_ID = "SELECT id FROM transaction_type WHERE name = ?";
        public static final String GET_ALL_TRANSACTIONS_FOR_USER = "SELECT id, user_id, wallet_id, transaction_type, " +
                "transaction_amount, asset_symbol, created_at, tt.name FROM transactions as t " +
                "JOIN transaction_type AS tt ON (tt.id=t.transaction_type) WHERE user_id = ?";
    }
}
