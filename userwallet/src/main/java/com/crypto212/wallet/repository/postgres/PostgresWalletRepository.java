package com.crypto212.wallet.repository.postgres;

import com.crypto212.wallet.repository.WalletRepository;
import com.crypto212.wallet.repository.entity.AssetEntity;
import com.crypto212.wallet.repository.entity.WalletAssetEntity;
import com.crypto212.wallet.repository.entity.WalletEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class PostgresWalletRepository implements WalletRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostgresWalletRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    public void updateUserAsset(Long walletId, Long assetId, String amount) {
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(Queries.UPDATE_USER_ASSET);
            ps.setString(1, amount);
            ps.setLong(2, walletId);
            ps.setLong(3, assetId);
            return ps;
        });
    }

    @Override
    public Optional<WalletAssetEntity> getUserWalletAsset(Long walletId, String assetSymbol) {
        List<WalletAssetEntity> walletAssetEntities = jdbcTemplate
                .query(Queries.GET_WALLET_ASSET, (rs, rowNum) -> walletAssetFromResultSet(rs), walletId, assetSymbol);
        return !walletAssetEntities.isEmpty() ? Optional.of(walletAssetEntities.get(0)) : Optional.empty();
    }

    private WalletAssetEntity walletAssetFromResultSet(ResultSet rs) throws SQLException {
        return WalletAssetEntity
                .builder()
                .assetEntity(
                        AssetEntity
                                .builder()
                                .id(rs.getLong("asset_id"))
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
        private static final String GET_WALLET_ASSET = "SELECT ws.user_id, ws.amount, a.asset_symbol, " +
                "a.id FROM wallets_assets as ws " +
                "JOIN assets as a ON (a.id=ws.asset_id) " +
                "WHERE ws.wallet_id = ? AND a.asset_symbol = ?";
        private static final String GET_ASSETS_FOR_WALLET = "SELECT ws.wallet_id, ws.asset_id, ws.amount," +
                " a.asset_name, a.asset_symbol, " +
                "a.created_at, a.updated_at FROM wallets_assets AS ws " +
                "JOIN assets AS a ON (a.id=ws.asset_id) " +
                "WHERE ws.wallet_id = ?";
    }
}
