package com.crypto212.privatewallet.service;

import com.crypto212.clients.privatewallet.AssetBalanceDTO;
import com.crypto212.privatewallet.service.dto.CompletedTransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Service
public class WalletService {
    private final ContractOperationsService contractOperationsService;

    public WalletService(ContractOperationsService contractOperationsService) {
        this.contractOperationsService = contractOperationsService;
    }

    public AssetBalanceDTO getWalletAsset(String assetSymbol) {
        BigDecimal result = null;
        try {
            result = contractOperationsService.getBalance(assetSymbol);
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
