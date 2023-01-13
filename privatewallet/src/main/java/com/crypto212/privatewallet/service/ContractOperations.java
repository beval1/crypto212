package com.crypto212.privatewallet.service;

import com.crypto212.privatewallet.conig.Tokens;
import com.crypto212.privatewallet.conig.WalletConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class ContractOperations {
    private final Web3j web3j;
    private final WalletConfig walletConfig;

    public ContractOperations(WalletConfig walletConfig) {
        this.walletConfig = walletConfig;
        String url = String.format("https://%s.infura.io/v3/%s",
                walletConfig.getProtocolNetwork(),
                walletConfig.getApiKey());
        log.info("Web3j url {}", url);
        this.web3j = Web3j.build(new HttpService(url));
    }

    public BigDecimal getBalance(String assetName) throws IOException {
        BigDecimal balance;
        if (assetName.equals("ETH")) {
            EthGetBalance ethBalance = web3j.ethGetBalance(walletConfig.getWalletAddress(), DefaultBlockParameterName.LATEST).send();
            balance = Convert.fromWei(ethBalance.getBalance().toString(), Convert.Unit.ETHER);
        } else {
            String tokenAddress = !walletConfig.getProtocolNetwork().equals("mainnet") ?
                    Tokens.testnetTokensList.get(assetName) : Tokens.mainnetTokensList.get(assetName);
            Function function = new Function(
                    "balanceOf",
                    List.of(new Address(walletConfig.getWalletAddress())),
                    List.of(new TypeReference<Uint256>() {}
                    ));
            String encodedFunction = FunctionEncoder.encode(function);
            EthCall ethCall = web3j.ethCall(
                            Transaction.createEthCallTransaction(walletConfig.getWalletAddress(), tokenAddress
                                    , encodedFunction),
                            DefaultBlockParameterName.LATEST)
                    .send();
            balance = new BigDecimal(Numeric.decodeQuantity(ethCall.getValue()));
            balance = Convert.fromWei(String.valueOf(balance), Convert.Unit.ETHER);
        }

        return balance;
    }
}
