package com.crypto212.userwallet.repository.postgres;

import com.crypto212.idgenerator.SnowFlake;
import com.crypto212.userwallet.repository.AssetRepository;
import com.crypto212.userwallet.repository.entity.AssetEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PostgresAssetRepository implements AssetRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SnowFlake snowFlake;

    public PostgresAssetRepository(JdbcTemplate jdbcTemplate, SnowFlake snowFlake) {
        this.jdbcTemplate = jdbcTemplate;
        this.snowFlake = snowFlake;
    }

    @Override
    public Optional<AssetEntity> getAssetByAssetSymbol(String assetSymbol) {
        List<AssetEntity> assetEntities = jdbcTemplate
                .query(Queries.GET_ASSET, (rs, rowNum) -> assetEntityFromResultSet(rs), assetSymbol);
        return !assetEntities.isEmpty() ? Optional.of(assetEntities.get(0)) : Optional.empty();
    }

    @Override
    public AssetEntity createAsset(String symbol, String assetName) {
        long assetId = snowFlake.nextId();
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    Queries.INSERT_ASSET);
            ps.setLong(1, assetId);
            ps.setString(2, symbol);
            ps.setString(3, assetName);
            ps.setTimestamp(4, currentTimestamp);
            ps.setTimestamp(5, currentTimestamp);
            return ps;
        });

        return new AssetEntity(assetId, symbol, assetName,
            currentTimestamp.toLocalDateTime(), currentTimestamp.toLocalDateTime());
    }

    @Override
    public List<AssetEntity> getAllAssets() {
        return jdbcTemplate.query(Queries.GET_ALL_ASSETS,
                (rs, rowNum) -> assetEntityFromResultSet(rs));
    }

    @Override
    public BigDecimal getTotalAssetBalance(String assetSymbol) {
        return jdbcTemplate.queryForObject(Queries.GET_ASSET_BALANCE,
                (rs, rowNum) -> rs.getBigDecimal("total_user_amount"), assetSymbol);
    }

    @Override
    public void updateAssetBalance(Long assetId, BigDecimal amount) {
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                    Queries.UPDATE_ASSET_BALANCE);
            ps.setBigDecimal(1, amount);
            ps.setLong(2, assetId);
            return ps;
        });
    }

    private AssetEntity assetEntityFromResultSet(ResultSet rs) throws SQLException {
        return AssetEntity
                .builder()
                .id(rs.getLong("id"))
                .assetSymbol(rs.getString("asset_symbol"))
                .assetName(rs.getString("asset_name"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }

    private static class Queries {
        private static final String INSERT_ASSET = "INSERT INTO assets(id, asset_symbol, asset_name, created_at, updated_at)" +
                " VALUES (?, ?, ?, ?, ?)";

        private static final String GET_ASSET = "SELECT id, asset_symbol, asset_name, created_at, updated_at " +
                "FROM assets WHERE asset_symbol = ?";
        private static final String GET_ALL_ASSETS = "SELECT id, asset_symbol, asset_name, created_at, updated_at" +
                " FROM assets";

        private static final String GET_ASSET_BALANCE = "SELECT total_user_amount FROM assets WHERE asset_symbol = ?";
        private static final String UPDATE_ASSET_BALANCE = "UPDATE assets SET total_user_amount = total_user_amount + ? " +
                "WHERE id = ?";
    }
}
