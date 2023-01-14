package com.crypto212.marketengine.service;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;
import com.crypto212.clients.privatewallet.AssetBalanceDTO;
import com.crypto212.clients.privatewallet.PrivateWalletClient;
import com.crypto212.clients.userwallet.TotalUserAssetDTO;
import com.crypto212.clients.userwallet.UserWalletClient;
import com.crypto212.marketengine.config.CurrencyPairs;
import com.crypto212.shared.exception.ApiException;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {
    private final PrivateWalletClient privateWalletClient;
    private final UserWalletClient userWalletClient;

    private final BinanceApiRestClient binanceApiRestClient;

    public OrderService(PrivateWalletClient privateWalletClient, UserWalletClient userWalletClient, BinanceApiRestClient binanceApiRestClient) {
        this.privateWalletClient = privateWalletClient;
        this.userWalletClient = userWalletClient;
        this.binanceApiRestClient = binanceApiRestClient;
    }

    @SneakyThrows
    public void exchange(Long walletId, String currencyPair, String actionType,
                          String amount, Long userId) {
        String baseCurrencyName = CurrencyPairs.currencyPairs.get(currencyPair).get(0);
        String quoteCurrencyName = CurrencyPairs.currencyPairs.get(currencyPair).get(1);

        //get the latest price of asset
        TickerStatistics tickerStatistics = binanceApiRestClient.get24HrPriceStatistics(currencyPair);
        BigDecimal latestPriceForBaseCurrencyInQuoteCurrency = new BigDecimal(tickerStatistics.getLastPrice());

        //precalculate exact baseCurrency and quoteCurrency amount
        BigDecimal orderBaseCurrencyAmount = new BigDecimal(amount);
        BigDecimal orderQuoteCurrencyAmount = orderBaseCurrencyAmount.multiply(latestPriceForBaseCurrencyInQuoteCurrency);

        if (actionType.equals("buy")){
            checkLiquidity(baseCurrencyName, orderBaseCurrencyAmount);
            userWalletClient.exchangeFunds(userId, walletId, baseCurrencyName, quoteCurrencyName,
                    orderBaseCurrencyAmount.toPlainString(), orderQuoteCurrencyAmount.toPlainString());
        } else if (actionType.equals("sell")){
            checkLiquidity(quoteCurrencyName, orderQuoteCurrencyAmount);
            userWalletClient.exchangeFunds(userId, walletId, quoteCurrencyName, baseCurrencyName,
                    orderQuoteCurrencyAmount.toPlainString(), orderBaseCurrencyAmount.toPlainString());
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid order");
        }

        //TODO: request to update ledger
    }

    private void checkLiquidity(String baseCurrencyName, BigDecimal amount){
        //get private wallet total asset amount
        ResponseEntity<AssetBalanceDTO> privateWalletResponse = privateWalletClient
                .getWalletAssetAmount(baseCurrencyName);
        BigDecimal totalBaseCurrencyBalance = new BigDecimal(privateWalletResponse.getBody().getBalance());

        //get user wallet total user assets
        ResponseEntity<TotalUserAssetDTO> userWalletResponse = userWalletClient
                .totalUserAssetBalance(baseCurrencyName);
        BigDecimal totalUserBaseCurrencyBalance = new BigDecimal(userWalletResponse.getBody().getBalance());

        //check if there's enough liquidity
        BigDecimal freeBalance = totalBaseCurrencyBalance.subtract(totalUserBaseCurrencyBalance);
        boolean enoughBaseCurrency = freeBalance.subtract(amount).compareTo(BigDecimal.ZERO) >= 1;

        if (!enoughBaseCurrency) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Not enough liquidity to create this order");
        }
    }

}
