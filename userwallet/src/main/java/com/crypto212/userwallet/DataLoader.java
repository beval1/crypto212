package com.crypto212.userwallet;

import com.crypto212.userwallet.repository.AssetRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private final AssetRepository assetRepository;

    public DataLoader(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (assetRepository.getAllAssets().isEmpty()) {
            assetRepository.createAsset("USDT", "USDT");
            assetRepository.createAsset("WETH", "Wrapped Ethereum");
            assetRepository.createAsset("ETH", "Ethereum");
        }
    }
}
