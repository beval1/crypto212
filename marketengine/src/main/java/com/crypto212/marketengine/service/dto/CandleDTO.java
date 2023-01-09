package com.crypto212.marketengine.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandleDTO {
    private String klineOpenTime;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String closePrice;
    private String volume;
    private String klineCloseTime;
    private String quoteAssetVolume;
    private String numberOfTrades;
    private String takerBuyAssetVolume;
    private String takerBuyQuoteAssetVolume;
}
