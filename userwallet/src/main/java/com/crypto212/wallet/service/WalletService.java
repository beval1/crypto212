package com.crypto212.wallet.service;

import com.crypto212.shared.exception.ApiException;
import com.crypto212.wallet.repository.WalletRepository;
import com.crypto212.wallet.repository.entity.WalletAssetEntity;
import com.crypto212.wallet.repository.entity.WalletEntity;
import com.crypto212.wallet.service.dto.WalletDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;

    public WalletService(WalletRepository walletRepository, ModelMapper modelMapper) {
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
    }

    public WalletDTO getWallet(Long userId) {
        WalletEntity walletEntity =  walletRepository.getWallet(userId).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Wallet Not Found!"));
        Set<WalletAssetEntity> walletAssetEntities = walletRepository.getAllAssetsForWallet(walletEntity.getId());
        walletEntity.setWalletAssets(walletAssetEntities);
        return modelMapper.map(walletEntity, WalletDTO.class);
    }

    public void subtract(Long walletId, String assetSymbol, String amountToSubtract) {
        WalletAssetEntity walletAssetEntity = walletRepository.getUserWalletAsset(walletId, assetSymbol).
                orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Asset not found!"));
        BigDecimal currentUserAmount = walletAssetEntity.getAmount();
        BigDecimal amountToSubtractDecimal = new BigDecimal(amountToSubtract);
        BigDecimal finalAmount = currentUserAmount.subtract(amountToSubtractDecimal);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "User does not have enough of asset amount to perform the operation");
        }
        walletRepository.updateUserAsset(walletId, walletAssetEntity.getAssetEntity().getId(), finalAmount.toPlainString());
    }

    public void add(Long walletId, String assetSymbol, String amount) {
        WalletAssetEntity walletAssetEntity = walletRepository.getUserWalletAsset(walletId, assetSymbol).
                orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Asset not found!"));
        BigDecimal currentUserAmount = walletAssetEntity.getAmount();
        BigDecimal amountToAdd = new BigDecimal(amount);
        BigDecimal finalAmount = currentUserAmount.add(amountToAdd);
        walletRepository.updateUserAsset(walletId, walletAssetEntity.getAssetEntity().getId(), finalAmount.toPlainString());
    }
}
