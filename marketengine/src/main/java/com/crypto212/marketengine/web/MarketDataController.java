package com.crypto212.marketengine.web;

import com.crypto212.marketengine.service.MarketDataService;
import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/market")
public class MarketDataController {
    private final MarketDataService marketDataService;

    public MarketDataController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/candles/{asset}")
    public ResponseEntity<ResponseDTO> getMarketCandles(@PathVariable("asset") String asset,
                                                        @RequestParam("interval") String interval,
                                                        @RequestParam("startTime") String startTime,
                                                        @RequestParam("endTime") String endTime) {
        String candleArrayDTO = marketDataService.getMarketCandlesForSymbol(asset, interval, startTime, endTime);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Fetched candles successfully!")
                        .content(candleArrayDTO)
                        .build());
    }
}
