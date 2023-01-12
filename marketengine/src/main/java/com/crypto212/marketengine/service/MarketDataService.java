package com.crypto212.marketengine.service;

import com.binance.connector.client.impl.spot.Market;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.LinkedHashMap;

@Service
public class MarketDataService {
    private final Market market;
    private final ObjectMapper objectMapper;

    public MarketDataService(Market market, ObjectMapper objectMapper) {
        this.market = market;
        this.objectMapper = objectMapper;
    }

    public String getMarketCandlesForSymbol(String asset, String interval, String startTime, String endTime)  {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("symbol", asset);
        params.put("interval", "1h");
        params.put("startTime", Date.valueOf("2023-01-08").getTime());
        params.put("endTime", Date.valueOf("2023-01-09").getTime());
        String response = market.klines(params);
//        try {
//            List<List<String>> list =  objectMapper.readValue(response, new TypeReference<>() {});
//            return objectMapper.readValue(response, CandleArrayDTO.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return null;
        return response;
    }
}
