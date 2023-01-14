package com.crypto212.marketengine.web;

import com.binance.api.client.domain.market.Candlestick;
import com.crypto212.marketengine.service.MarketDataService;
import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market")
public class MarketDataController {
    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/candles/{asset}")
    public ResponseEntity<ResponseDTO> getMarketCandles(@PathVariable("asset") String assetPair,
                                                        @RequestParam("interval") String interval,
                                                        @RequestParam("startTime") String startTime,
                                                        @RequestParam("endTime") String endTime,
                                                        @RequestParam("limit") int limit) {
        List<Candlestick> candlesticks = marketDataService.getMarketCandlesForSymbol(assetPair, interval, startTime, endTime, limit);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Fetched candles successfully!")
                        .content(candlesticks)
                        .build());
    }
}
