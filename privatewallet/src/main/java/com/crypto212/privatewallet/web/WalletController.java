package com.crypto212.privatewallet.web;

import com.crypto212.clients.privatewallet.AssetBalanceDTO;
import com.crypto212.privatewallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/private-wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{assetName}")
    public ResponseEntity<AssetBalanceDTO> getWalletAssetAmount(@PathVariable("assetName") String assetName){
        AssetBalanceDTO assetBalanceDTO = walletService.getWalletAsset(assetName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(assetBalanceDTO);
    }
}
