package com.crypto212.wallet.repository;

import com.crypto212.wallet.repository.entity.WalletAssetEntity;
import com.crypto212.wallet.repository.entity.WalletEntity;

import java.util.Optional;
import java.util.Set;

public interface WalletRepository {
    Optional<WalletEntity> getWallet(Long userId);
    Set<WalletAssetEntity> getAllAssetsForWallet(Long walletId);

    void updateUserAsset(Long walletId, Long assetId, String amount);

    Optional<WalletAssetEntity> getUserWalletAsset(Long walletId, String assetSymbol);
}
