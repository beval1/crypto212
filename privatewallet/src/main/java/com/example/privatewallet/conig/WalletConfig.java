package com.example.privatewallet.conig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class WalletConfig {
    @Value("${app.web3.infura-api-key}")
    private String apiKey;
    @Value("${app.web3.network}")
    private String protocolNetwork;
    @Value("${app.web3.wallet-address}")
    private String walletAddress;
}
