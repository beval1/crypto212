package com.crypto212.userwallet.repository;

import com.crypto212.userwallet.repository.entity.AssetEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AssetRepository {
    Optional<AssetEntity> getAssetByAssetSymbol(String assetSymbol);
    AssetEntity createAsset(String symbol, String assetName);
    List<AssetEntity> getAllAssets();
    BigDecimal getTotalAssetBalance(String assetSymbol);

    void updateAssetBalance(Long assetId, BigDecimal amount);
}
