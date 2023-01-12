package com.crypto212.privatewallet.service;

import com.crypto212.clients.privatewallet.AssetBalanceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
public class WalletService {
    private final ContractOperations contractOperations;

    public WalletService(ContractOperations contractOperations) {
        this.contractOperations = contractOperations;
    }

    public AssetBalanceDTO getWalletAsset(String assetName) {
        BigDecimal result = null;
        try {
            result = contractOperations.getBalance(assetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("{} balance for {}", assetName, result);
        return new AssetBalanceDTO(result);
    }
}
