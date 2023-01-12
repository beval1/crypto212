package com.crypto212.wallet.web;

import com.crypto212.shared.dto.ResponseDTO;
import com.crypto212.wallet.service.AssetService;
import com.crypto212.wallet.service.dto.AssetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllAssets(){
        List<AssetDTO> assetDTOS = assetService.getAllAssets();
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ResponseDTO.builder()
                        .content(assetDTOS)
                        .build()
                );
    }
}
