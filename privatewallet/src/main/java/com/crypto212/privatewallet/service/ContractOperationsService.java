package com.crypto212.privatewallet.service;

import com.crypto212.privatewallet.conig.Tokens;
import com.crypto212.privatewallet.conig.WalletConfig;
import com.crypto212.privatewallet.service.dto.CompletedTransactionDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class ContractOperationsService {
    private final Web3j web3j;
    private final WalletConfig walletConfig;

    public ContractOperationsService(WalletConfig walletConfig) {
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
                    List.of(new TypeReference<Uint256>() {
                            }
                    ));
            String encodedFunction = FunctionEncoder.encode(function);
            EthCall ethCall = web3j.ethCall(
                            Transaction.createEthCallTransaction(walletConfig.getWalletAddress(), tokenAddress
                                    , encodedFunction),
                            DefaultBlockParameterName.LATEST)
                    .send();
            balance = new BigDecimal(Numeric.decodeQuantity(ethCall.getValue()));
            balance = Convert.fromWei(String.valueOf(balance), Tokens.testnetTokensUnit.get(assetName));
        }

        return balance;
    }

    @SneakyThrows
    public CompletedTransactionDTO withdraw(String assetSymbol, String toAddress, String amount) {
        Credentials creds = Credentials.create(walletConfig.getWalletPrivateKey());
        if (assetSymbol.equals("ETH")) {
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3j, creds, toAddress,
                    new BigDecimal(amount), Convert.Unit.ETHER).send();
            transactionReceipt.getTransactionHash();
            return new CompletedTransactionDTO(transactionReceipt.getTransactionHash());
        }

        RawTransactionManager manager = new RawTransactionManager(web3j, creds);
        String contractAddress = Tokens.testnetTokensList.get(assetSymbol);
        BigDecimal tokenUnitFactor = Tokens.testnetTokensUnit.get(assetSymbol).getWeiFactor();
        BigDecimal unitFactor = BigDecimal.valueOf(tokenUnitFactor.doubleValue());
        BigInteger totalSum = new BigDecimal(amount)
                .multiply(unitFactor)
                .toBigInteger();
        String data = encodeTransferData(toAddress, totalSum);
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        BigInteger gasLimit = BigInteger.valueOf(120000); // set gas limit here
        EthSendTransaction transaction = manager.sendTransaction(gasPrice, gasLimit, contractAddress, data, null);
        return new CompletedTransactionDTO(transaction.getTransactionHash());
    }

    public static String encodeTransferData(String toAddress, BigInteger sum) {
        Function function = new Function(
                "transfer",  // function we're calling
                Arrays.asList(new Address(toAddress), new Uint256(sum)),  // Parameters to pass as Solidity Types
                Arrays.asList(new org.web3j.abi.TypeReference<Bool>() {
                }));
        return FunctionEncoder.encode(function);
    }
}
