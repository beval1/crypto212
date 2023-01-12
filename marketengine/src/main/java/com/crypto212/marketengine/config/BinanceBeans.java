package com.crypto212.marketengine.config;

import com.binance.connector.client.SpotClient;
import com.binance.connector.client.impl.SpotClientImpl;
import com.binance.connector.client.impl.spot.Market;
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
    public Market market(){
        return new Market(baseUrl, apiKey, true);
    }

    @Bean
    public SpotClient spotClient(){
        return new SpotClientImpl(baseUrl, secretKey);
    }
}
