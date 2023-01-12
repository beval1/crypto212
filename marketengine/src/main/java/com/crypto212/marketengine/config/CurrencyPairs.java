package com.crypto212.marketengine.config;

import java.util.List;
import java.util.Map;

public class CurrencyPairs {
    private CurrencyPairs() {}

    public static final Map<String, List<String>> currencyPairs = Map.ofEntries(
            Map.entry("ETHUSDT", List.of("ETH", "USDT"))
    );
}
