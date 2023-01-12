package com.crypto212.marketengine.service;

import com.binance.connector.client.impl.spot.Market;
import com.crypto212.clients.privatewallet.AssetBalanceDTO;
import com.crypto212.clients.privatewallet.PrivateWalletClient;
import com.crypto212.clients.userwallet.TotalUserAssetDTO;
import com.crypto212.clients.userwallet.UserWalletClient;
import com.crypto212.marketengine.config.CurrencyPairs;
import com.crypto212.marketengine.service.dto.AvgPriceWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Objects;

@Service
public class OrderService {
    private final Market market;
    private final PrivateWalletClient privateWalletClient;
    private final UserWalletClient userWalletClient;
    private final ObjectMapper objectMapper;

    public OrderService(Market market, PrivateWalletClient privateWalletClient, UserWalletClient userWalletClient, ObjectMapper objectMapper) {
        this.market = market;
        this.privateWalletClient = privateWalletClient;
        this.userWalletClient = userWalletClient;
        this.objectMapper = objectMapper;
    }

    public void createOrder(Long userId, String currencyPair, String actionType, String amount) {
        String baseCurrencyName = CurrencyPairs.currencyPairs.get(currencyPair).get(0);
        String quoteCurrencyName = CurrencyPairs.currencyPairs.get(currencyPair).get(1);
        if (Objects.equals(actionType, "buy")){
            createBuyOrder(userId, currencyPair, baseCurrencyName, quoteCurrencyName, amount);
        } else if (actionType.equals("sell")){
            createSellOrder(userId, currencyPair, baseCurrencyName, quoteCurrencyName, amount);
        }
    }

    public void createBuyOrder(Long userId, String currencyPair, String baseCurrency, String quoteCurrency, String amount) {
        exchange(userId, currencyPair, baseCurrency, quoteCurrency, amount);
    }


    public void createSellOrder(Long userId, String currencyPair, String baseCurrency, String quoteCurrency, String amount) {
        exchange(userId, currencyPair, quoteCurrency, baseCurrency, amount);
    }

    @SneakyThrows
    private void exchange(Long userId, String currencyPair, String baseCurrencyName, String quoteCurrencyName,
                          String amount){
        //get current avg Price of asset
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", currencyPair);
        AvgPriceWrapper avgPriceWrapper = objectMapper.readValue(market.averagePrice(params), AvgPriceWrapper.class);
        BigDecimal avgPriceForBaseCurrency = BigDecimal.valueOf(avgPriceWrapper.getData().getPrice());

        //precalculate exact baseCurrency and quoteCurrency amount
        BigDecimal orderBaseCurrencyAmount = new BigDecimal(amount);
        BigDecimal orderQuoteCurrencyAmount = orderBaseCurrencyAmount.multiply(avgPriceForBaseCurrency);

        //get private wallet total asset amount
        ResponseEntity<AssetBalanceDTO> privateWalletResponse = privateWalletClient
                .getWalletAssetAmount(baseCurrencyName);
        BigDecimal totalBaseCurrencyBalance = privateWalletResponse.getBody().getBalance();

        //get user wallet total user assets
        ResponseEntity<TotalUserAssetDTO> userWalletResponse =  userWalletClient
                .totalUserAssetBalance(baseCurrencyName);
        BigDecimal totalUserBaseCurrencyBalance = userWalletResponse.getBody().getBalance();

        //check if there's enough liquidity
        BigDecimal freeBalance = totalBaseCurrencyBalance.subtract(totalUserBaseCurrencyBalance);
        boolean enoughBaseCurrency = freeBalance.subtract(orderBaseCurrencyAmount).compareTo(BigDecimal.ZERO) >= 1;

        //if possible, execute exchange on userWallet
        if (enoughBaseCurrency) {
            userWalletClient.exchangeFunds(userId, baseCurrencyName, quoteCurrencyName,
                    orderBaseCurrencyAmount.toString(), orderQuoteCurrencyAmount.toString());
        }

        //TODO: request to update ledger
    }
}
