package com.crypto212.userwallet.web;

import com.crypto212.clients.userwallet.TotalUserAssetDTO;
import com.crypto212.shared.dto.ResponseDTO;
import com.crypto212.userwallet.service.AssetService;
import com.crypto212.userwallet.service.dto.AssetDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/userwallet/assets")
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

    @GetMapping("/{assetSymbol}")
    public ResponseEntity<TotalUserAssetDTO> totalUserAssetBalance(@PathVariable("assetSymbol") String assetSymbol) {
        TotalUserAssetDTO totalUserAssetDTO = assetService.totalUserAssetBalance(assetSymbol);
        return ResponseEntity.status(HttpStatus.OK)
                .body(totalUserAssetDTO);
    }
}
