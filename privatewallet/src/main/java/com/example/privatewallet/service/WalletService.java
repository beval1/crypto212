package com.example.privatewallet.service;

import com.example.privatewallet.ContractOperations;
import com.example.privatewallet.conig.WalletConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WalletService {
    private final ContractOperations contractOperations;
    private final WalletConfig walletConfig;

    public WalletService(ContractOperations contractOperations, WalletConfig walletConfig) {
        this.contractOperations = contractOperations;
        this.walletConfig = walletConfig;
    }

    public String getWalletAssets() {
        log.info("Wallet address: {}", walletConfig.getWalletAddress());
        String result = String.valueOf(contractOperations.getBalance(walletConfig.getWalletAddress()));
        log.info("Result {}", result);
        return result;
    }
}
