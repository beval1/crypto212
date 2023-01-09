package com.example.privatewallet;

import com.example.privatewallet.conig.WalletConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class ContractOperations {
    private final Web3j web3j;

    public ContractOperations(WalletConfig walletConfig) {
        String url = String.format("https://%s.infura.io/v3/%s",
                walletConfig.getProtocolNetwork(),
                walletConfig.getApiKey());
        log.info("Web3j url {}", url);
        this.web3j = Web3j.build(new HttpService(url));
    }

    public BigInteger getBalance(String fromAddress){
        BigInteger balance = null;
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(fromAddress, DefaultBlockParameterName.LATEST)
                    .sendAsync().get();
            balance = ethGetBalance.getBalance();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("address " + fromAddress + " balance " + balance + " wei");
        return balance;
    }
}
