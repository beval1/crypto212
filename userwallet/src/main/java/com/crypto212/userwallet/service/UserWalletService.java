package com.crypto212.userwallet.service;

import com.crypto212.shared.exception.ApiException;
import com.crypto212.userwallet.repository.AssetRepository;
import com.crypto212.userwallet.repository.WalletRepository;
import com.crypto212.userwallet.repository.entity.AssetEntity;
import com.crypto212.userwallet.repository.entity.WalletAssetEntity;
import com.crypto212.userwallet.repository.entity.WalletEntity;
import com.crypto212.userwallet.service.dto.WalletDTO;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class UserWalletService {
    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final TransactionTemplate transactionTemplate;
    private final AssetRepository assetRepository;

    public UserWalletService(WalletRepository walletRepository, ModelMapper modelMapper, TransactionTemplate transactionTemplate, AssetRepository assetRepository) {
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
        this.transactionTemplate = transactionTemplate;
        this.assetRepository = assetRepository;
    }

    public WalletDTO getWallet(Long userId) {
        //get user wallet or create new one
        WalletEntity walletEntity = walletRepository.getWallet(userId).orElseGet(() -> createWallerForUser(userId));
        Set<WalletAssetEntity> walletAssetEntities = walletRepository.getAllAssetsForWallet(walletEntity.getId());
        walletEntity.setWalletAssets(walletAssetEntities);
        return modelMapper.map(walletEntity, WalletDTO.class);
    }

    private WalletEntity createWallerForUser(Long userId) {
        return walletRepository.createWalletForUser(userId);
    }
    private WalletAssetEntity createWalletAssetForUser(Long walletId, String assetSymbol) {
        AssetEntity assetEntity = assetRepository.getAssetByAssetSymbol(assetSymbol).orElseThrow(
                () -> new ApiException(HttpStatus.BAD_REQUEST, "Asset doesn't exist!"));
        return walletRepository.createWalletAssetForUser(walletId, assetEntity);
    }

    public void exchange(Long walletId, String baseCurrency, String quoteCurrency, String baseCurrencyAmountToBuy,
                         String quoteCurrencyAmountToSell) {
        //create asset if not exists
        WalletAssetEntity baseCurrencyAsset = walletRepository.getUserWalletAsset(walletId, baseCurrency).
                orElseGet(() -> createWalletAssetForUser(walletId, baseCurrency));
        BigDecimal currentUserBaseCurrencyAmount = baseCurrencyAsset.getAmount();
        BigDecimal amountToBuy = new BigDecimal(baseCurrencyAmountToBuy);
        BigDecimal finalBaseCurrencyAmount = currentUserBaseCurrencyAmount.add(amountToBuy);

        WalletAssetEntity quoteCurrencyAsset = walletRepository.getUserWalletAsset(walletId, quoteCurrency).
                orElseGet(() -> createWalletAssetForUser(walletId, quoteCurrency));
        BigDecimal currentQuoteCurrencyAmount = quoteCurrencyAsset.getAmount();
        BigDecimal amountToSell = new BigDecimal(quoteCurrencyAmountToSell);
        BigDecimal finalQuoteCurrencyAmount = currentQuoteCurrencyAmount.subtract(amountToSell);
        if (finalQuoteCurrencyAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    String.format("User does not have enough of %s amount to perform the operation", quoteCurrency));
        }

        BigDecimal totalBaseCurrencyUserAmount = assetRepository.getTotalAssetBalance(baseCurrency);
        BigDecimal totalQuoteCurrencyUserAmount = assetRepository.getTotalAssetBalance(quoteCurrency);
        transactionTemplate.execute(status -> {
            walletRepository.updateUserAsset(walletId, baseCurrencyAsset.getAssetEntity().getId(), finalBaseCurrencyAmount);
            walletRepository.updateUserAsset(walletId, quoteCurrencyAsset.getAssetEntity().getId(), finalQuoteCurrencyAmount);
            assetRepository.updateAssetBalance(baseCurrencyAsset.getAssetEntity().getId(),
                    totalBaseCurrencyUserAmount.add(amountToBuy));
            assetRepository.updateAssetBalance(quoteCurrencyAsset.getAssetEntity().getId(),
                    totalQuoteCurrencyUserAmount.subtract(amountToSell));
            return status;
        });
    }

    public void addAssets(Long userId, String assetSymbol, String amount) {
        WalletEntity walletEntity = walletRepository.getWallet(userId).orElseGet(() -> createWallerForUser(userId));
        Long walletId = walletEntity.getId();
        WalletAssetEntity userCurrencyAsset = walletRepository.getUserWalletAsset(walletId, assetSymbol).
                orElseGet(() -> createWalletAssetForUser(walletId, assetSymbol));
        BigDecimal totalCurrencyUserAmount = assetRepository.getTotalAssetBalance(assetSymbol);
        transactionTemplate.execute(status -> {
            walletRepository.updateUserAsset(walletId, userCurrencyAsset.getAssetEntity().getId(),
                    userCurrencyAsset.getAmount().add(new BigDecimal(amount)));
            assetRepository.updateAssetBalance(userCurrencyAsset.getAssetEntity().getId(),
                    totalCurrencyUserAmount.add(new BigDecimal(amount)));
            return status;
        });
    }
}
