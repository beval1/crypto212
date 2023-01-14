package com.crypto212.marketengine.config;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.impl.BinanceApiRestClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinanceBeans {
    @Value("${app.binance.baseUrl}")
    private String baseUrl;
    @Value("${app.binance.apiKey}")
    private String apiKey;
    @Value("${app.binance.secretKey}")
    private String secretKey;

    @Bean
    public BinanceApiRestClient binanceApiRestClient(){
        return new BinanceApiRestClientImpl(apiKey, secretKey);
    }
}
