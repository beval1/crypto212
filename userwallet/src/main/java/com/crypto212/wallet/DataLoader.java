package com.crypto212.wallet;

import com.crypto212.wallet.repository.AssetRepository;
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
            assetRepository.createAsset("USDC", "USDC");
            assetRepository.createAsset("WETH", "Wrapped Ethereum");
            assetRepository.createAsset("ETH", "Ethereum");
        }
    }
}
