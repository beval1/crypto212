package com.crypto212.userwallet.service;

import com.crypto212.clients.userwallet.TotalUserAssetDTO;
import com.crypto212.userwallet.repository.AssetRepository;
import com.crypto212.userwallet.repository.entity.AssetEntity;
import com.crypto212.userwallet.service.dto.AssetDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final ModelMapper modelMapper;

    public AssetService(AssetRepository assetRepository, ModelMapper modelMapper) {
        this.assetRepository = assetRepository;
        this.modelMapper = modelMapper;
    }

    public List<AssetDTO> getAllAssets(){
        List<AssetEntity> assetEntities =  assetRepository.getAllAssets();
        return Arrays.asList(modelMapper.map(assetEntities, AssetDTO[].class));
    }

    public TotalUserAssetDTO totalUserAssetBalance(String assetSymbol) {
        BigDecimal balance = assetRepository.getTotalAssetBalance(assetSymbol);
        return new TotalUserAssetDTO(balance.toPlainString());
    }
}
