package com.crypto212.wallet.repository;

import com.crypto212.wallet.repository.entity.AssetEntity;

import java.util.Optional;

public interface AssetRepository {
    Optional<AssetEntity> getAssetByAssetSymbol();
}
