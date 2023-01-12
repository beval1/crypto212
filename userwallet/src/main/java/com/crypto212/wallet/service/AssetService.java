package com.crypto212.wallet.service;

import com.crypto212.wallet.repository.AssetRepository;
import com.crypto212.wallet.repository.entity.AssetEntity;
import com.crypto212.wallet.service.dto.AssetDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
