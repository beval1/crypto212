package com.crypto212.privatewallet.service;

import com.crypto212.clients.privatewallet.AssetBalanceDTO;
import com.crypto212.privatewallet.repository.RedisRepository;
import com.crypto212.privatewallet.service.dto.CompletedTransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
public class WalletService {
    private final ContractOperationsService contractOperationsService;
    private final RedisRepository redisRepository;

    public WalletService(ContractOperationsService contractOperationsService, RedisRepository redisRepository) {
        this.contractOperationsService = contractOperationsService;
        this.redisRepository = redisRepository;
    }

    public AssetBalanceDTO getWalletAsset(String assetSymbol) {
        BigDecimal cachedAssetBalance = redisRepository.getData(assetSymbol);
        if (cachedAssetBalance != null) {
            return new AssetBalanceDTO(cachedAssetBalance.toPlainString());
        }

        BigDecimal result = null;
        try {
            result = contractOperationsService.getBalance(assetSymbol);
            redisRepository.insertData(assetSymbol, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("{} balance for {}", assetSymbol, result);
        return new AssetBalanceDTO(result.toPlainString());
    }

    public CompletedTransactionDTO withdraw(String assetSymbol, String address, String amount) {
        return contractOperationsService.withdraw(assetSymbol, address, amount);
    }
}
