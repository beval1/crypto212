package com.crypto212.userwallet.repository;

import com.crypto212.userwallet.repository.entity.AssetEntity;
import com.crypto212.userwallet.repository.entity.WalletAssetEntity;
import com.crypto212.userwallet.repository.entity.WalletEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

public interface WalletRepository {
    Optional<WalletEntity> getWallet(Long userId);
    Set<WalletAssetEntity> getAllAssetsForWallet(Long walletId);

    void updateUserAsset(Long walletId, Long assetId, BigDecimal amount);

    Optional<WalletAssetEntity> getUserWalletAsset(Long walletId, String assetSymbol);

    WalletEntity createWalletForUser(Long userId);

    WalletAssetEntity createWalletAssetForUser(Long walletId, AssetEntity assetEntity);
}
