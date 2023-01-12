package com.crypto212.wallet.service;

import com.crypto212.clients.userwallet.TotalUserAssetDTO;
import com.crypto212.clients.userwallet.UserWalletClient;
import com.crypto212.shared.exception.ApiException;
import com.crypto212.wallet.repository.WalletRepository;
import com.crypto212.wallet.repository.entity.WalletAssetEntity;
import com.crypto212.wallet.repository.entity.WalletEntity;
import com.crypto212.wallet.service.dto.WalletDTO;
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

    public UserWalletService(WalletRepository walletRepository, ModelMapper modelMapper, TransactionTemplate transactionTemplate) {
        this.walletRepository = walletRepository;
        this.modelMapper = modelMapper;
        this.transactionTemplate = transactionTemplate;
    }

    public WalletDTO getWallet(Long userId) {
        WalletEntity walletEntity = walletRepository.getWallet(userId).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "Wallet Not Found!"));
        Set<WalletAssetEntity> walletAssetEntities = walletRepository.getAllAssetsForWallet(walletEntity.getId());
        walletEntity.setWalletAssets(walletAssetEntities);
        return modelMapper.map(walletEntity, WalletDTO.class);
    }

    public void exchange(Long walletId, String baseCurrency, String quoteCurrency, String baseCurrencyAmountToBuy,
                         String quoteCurrencyAmountToSell) {
        WalletAssetEntity baseCurrencyAsset = walletRepository.getUserWalletAsset(walletId, baseCurrency).
                orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Asset not found!"));
        BigDecimal currentUserBaseCurrencyAmount = baseCurrencyAsset.getAmount();
        BigDecimal amountToBuy = new BigDecimal(baseCurrencyAmountToBuy);
        BigDecimal finalBaseCurrencyAmount = currentUserBaseCurrencyAmount.add(amountToBuy);

        WalletAssetEntity quoteCurrencyAsset = walletRepository.getUserWalletAsset(walletId, baseCurrency).
                orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Asset not found!"));
        BigDecimal currentQuoteCurrencyAmount = quoteCurrencyAsset.getAmount();
        BigDecimal amountToSell = new BigDecimal(quoteCurrencyAmountToSell);
        BigDecimal finalQuoteCurrencyAmount = currentQuoteCurrencyAmount.subtract(amountToSell);
        if (finalQuoteCurrencyAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    String.format("User does not have enough of %s amount to perform the operation", quoteCurrency));
        }

        transactionTemplate.execute((status) -> {
            walletRepository.updateUserAsset(walletId, baseCurrencyAsset.getAssetEntity().getId(), finalBaseCurrencyAmount);
            walletRepository.updateUserAsset(walletId, quoteCurrencyAsset.getAssetEntity().getId(), finalQuoteCurrencyAmount);
            return status;
        });
    }

    public TotalUserAssetDTO totalUserAssetBalance(String assetName) {
        BigDecimal balance = walletRepository.getTotalUserAssetBalance(assetName);
        return new TotalUserAssetDTO(balance);
    }
}
