package com.crypto212.userwallet.repository.postgres;

import com.crypto212.idgenerator.SnowFlake;
import com.crypto212.userwallet.repository.WalletRepository;
import com.crypto212.userwallet.repository.entity.AssetEntity;
import com.crypto212.userwallet.repository.entity.WalletAssetEntity;
import com.crypto212.userwallet.repository.entity.WalletEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
public class PostgresWalletRepository implements WalletRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SnowFlake snowFlake;

    public PostgresWalletRepository(JdbcTemplate jdbcTemplate, SnowFlake snowFlake) {
        this.jdbcTemplate = jdbcTemplate;
        this.snowFlake = snowFlake;
    }

    @Override
    public Optional<WalletEntity> getWallet(Long userId) {
        List<WalletEntity> walletEntities = jdbcTemplate
                .query(Queries.GET_USER_WALLET, (rs, rowNum) -> walletFromResultSet(rs), userId);
        return !walletEntities.isEmpty() ? Optional.of(walletEntities.get(0)) : Optional.empty();
    }

    @Override
    public Set<WalletAssetEntity> getAllAssetsForWallet(Long walletId) {
        List<WalletAssetEntity> walletEntities = jdbcTemplate.query(Queries.GET_ASSETS_FOR_WALLET,
                (rs, rowNum) -> walletAssetFromResultSet(rs), walletId);
        return new HashSet<>(walletEntities);
    }

    @Override
    public void updateUserAsset(Long walletId, Long assetId, BigDecimal amount) {
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(Queries.UPDATE_USER_ASSET);
            ps.setBigDecimal(1, amount);
            ps.setLong(2, walletId);
            ps.setLong(3, assetId);
            return ps;
        });
    }

    @Override
    public Optional<WalletAssetEntity> getUserWalletAsset(Long walletId, String assetSymbol) {
        List<WalletAssetEntity> walletAssetEntities = jdbcTemplate
                .query(Queries.GET_USER_WALLET_ASSET, (rs, rowNum) -> walletAssetFromResultSet(rs), walletId, assetSymbol);
        return !walletAssetEntities.isEmpty() ? Optional.of(walletAssetEntities.get(0)) : Optional.empty();
    }

    @Override
    public WalletEntity createWalletForUser(Long userId) {
        long id = snowFlake.nextId();
        LocalDateTime currenTime = LocalDateTime.now();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(Queries.CREATE_USER_WALLET);
            ps.setLong(1, id);
            ps.setLong(2, userId);
            ps.setTimestamp(3, Timestamp.valueOf(currenTime));
            ps.setTimestamp(4, Timestamp.valueOf(currenTime));
            return ps;
        });
        return WalletEntity
                .builder()
                .id(id)
                .userId(userId)
                .createdAt(currenTime)
                .updatedAt(currenTime)
                .build();
    }

    @Override
    public WalletAssetEntity createWalletAssetForUser(Long walletId, AssetEntity assetEntity) {
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(Queries.CREATE_USER_WALLET_ASSET);
            ps.setLong(1, walletId);
            ps.setLong(2, assetEntity.getId());
            ps.setBigDecimal(3, BigDecimal.ZERO);
            return ps;
        });
        //TODO: fix relation
        return WalletAssetEntity
                .builder()
                .assetEntity(assetEntity)
                .amount(BigDecimal.ZERO)
                .build();
    }


    private WalletAssetEntity walletAssetFromResultSet(ResultSet rs) throws SQLException {
        return WalletAssetEntity
                .builder()
                .assetEntity(
                        AssetEntity
                                .builder()
                                .id(rs.getLong("id"))
                                .assetName(rs.getString("asset_name"))
                                .assetSymbol(rs.getString("asset_symbol"))
                                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                                .build()
                )
                .amount(rs.getBigDecimal("amount"))
                .build();
    }

    private WalletEntity walletFromResultSet(ResultSet rs) throws SQLException {
        return WalletEntity
                .builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }

    private static class Queries {
        private static final String UPDATE_USER_ASSET = "UPDATE  wallets_assets SET amount = ? " +
                "WHERE wallet_id = ? AND asset_id = ?";
        private static final String GET_USER_WALLET = "SELECT id, user_id, created_at, updated_at " +
                "FROM wallets " +
                "WHERE user_id = ?";

        private static final String CREATE_USER_WALLET = "INSERT INTO wallets(id, user_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?)";

        private static final String CREATE_USER_WALLET_ASSET = "INSERT INTO wallets_assets(wallet_id, asset_id, amount) " +
                "VALUES (?, ?, ?)";
        private static final String GET_USER_WALLET_ASSET = "SELECT ws.amount, a.asset_symbol, " +
                "a.id, a.asset_name, a.created_at, a.updated_at FROM wallets_assets as ws " +
                "JOIN assets as a ON (a.id=ws.asset_id) " +
                "WHERE ws.wallet_id = ? AND a.asset_symbol = ?";
        private static final String GET_ASSETS_FOR_WALLET = "SELECT ws.wallet_id, ws.asset_id, ws.amount, " +
                "a.id,  a.asset_name, a.asset_symbol, a.created_at, a.updated_at FROM wallets_assets AS ws " +
                "JOIN assets AS a ON (a.id=ws.asset_id) " +
                "WHERE ws.wallet_id = ?";
    }
}
