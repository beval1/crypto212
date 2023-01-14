package com.crypto212.marketengine.service;

import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.crypto212.shared.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Service
public class MarketDataService {
    private final BinanceApiRestClient binanceApiRestClient;

    public MarketDataService(BinanceApiRestClient binanceApiRestClient) {
        this.binanceApiRestClient = binanceApiRestClient;
    }

    public List<Candlestick> getMarketCandlesForSymbol(String asset, String interval, String startTime,
                                                       String endTime, int limit) {
        long startTimeLong = Date.valueOf(startTime).getTime();
        long endTimeLong = Date.valueOf(endTime).getTime();
        CandlestickInterval candlestickInterval = Arrays.stream(CandlestickInterval.values())
                .filter(constant -> constant.getIntervalId().equals(interval))
                .findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Bad Interval"));
        return binanceApiRestClient.getCandlestickBars(asset, candlestickInterval, limit, startTimeLong, endTimeLong);
    }
}
