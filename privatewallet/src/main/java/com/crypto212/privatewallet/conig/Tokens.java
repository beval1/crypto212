package com.crypto212.privatewallet.conig;

import org.web3j.utils.Convert;

import java.util.Map;

public class Tokens {
    private Tokens() {
    }

    public static final Map<String, String> mainnetTokensList = Map.ofEntries(
            Map.entry("MATIC", "0xcc42724c6683b7e57334c4e856f4c9965ed682bd"),
            Map.entry("USDT", "0x55d398326f99059ff775485246999027b3197955"),
            Map.entry("BNB", "0x242a1ff6ee06f2131b7924cacb74c7f9e3a5edc9")
    );


    public static final Map<String, String> testnetTokensList = Map.ofEntries(
            Map.entry("WETH", "0xB4FBF271143F4FBf7B91A5ded31805e42b2208d6"),
            Map.entry("UNI", "0x1f9840a85d5aF5bf1D1762F925BDADdC4201F984"),
            Map.entry("USDT", "0x07865c6e87b9f70255377e024ace6630c1eaa37f")
    );

    public static final Map<String, Convert.Unit> testnetTokensUnit = Map.ofEntries(
            Map.entry("WETH", Convert.Unit.ETHER),
            Map.entry("UNI", Convert.Unit.ETHER),
            Map.entry("USDT", Convert.Unit.MWEI)
    );
}
