package com.crypto212.privatewallet.web;

import com.crypto212.clients.privatewallet.AssetBalanceDTO;
import com.crypto212.privatewallet.service.WalletService;
import com.crypto212.privatewallet.service.dto.CompletedTransactionDTO;
import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/private-wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{assetSymbol}")
    public ResponseEntity<AssetBalanceDTO> getWalletAssetAmount(@PathVariable("assetSymbol") String assetSymbol){
        AssetBalanceDTO assetBalanceDTO = walletService.getWalletAsset(assetSymbol);
        return ResponseEntity.status(HttpStatus.OK)
                .body(assetBalanceDTO);
    }

    @PostMapping("/withdraw/{assetSymbol}")
    public ResponseEntity<ResponseDTO> withdraw(@PathVariable("assetSymbol") String assetSymbol,
                                                @RequestParam("amount") String amount,
                                                @RequestParam("address") String address){
        CompletedTransactionDTO completedTransactionDTO = walletService.withdraw(assetSymbol, address, amount);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO
                        .builder()
                        .content(completedTransactionDTO)
                        .message("Assets withdrawn successfully!")
                        .build());
    }
}
