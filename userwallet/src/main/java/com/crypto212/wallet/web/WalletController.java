package com.crypto212.wallet.web;

import com.crypto212.shared.dto.ResponseDTO;
import com.crypto212.wallet.service.WalletService;
import com.crypto212.wallet.service.dto.WalletDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PreAuthorize("#header == #userId")
    @GetMapping
    public ResponseEntity<ResponseDTO> getWallet(@RequestParam("userId") Long userId,
                                                 @RequestHeader("X-User-Id") Long header){
        WalletDTO walletDTO = walletService.getWallet(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Wallet fetched successfully!")
                        .content(walletDTO)
                        .build());
    }

    @PostMapping("/{walletId}/subtract")
    public ResponseEntity<ResponseDTO> subtractFunds(@PathVariable("walletId") Long walletId,
                                                 @RequestParam("assetSymbol") String assetSymbol,
                                                 @RequestParam("amount") String amount){
        walletService.subtract(walletId, assetSymbol, amount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Funds subtracted successfully")
                        .content(null)
                        .build());
    }

    @PostMapping("/{walletId}/add")
    public ResponseEntity<ResponseDTO> addFunds(@PathVariable("walletId") Long walletId,
                                                 @RequestParam("assetSymbol") String assetSymbol,
                                                 @RequestParam("amount") String amount){
        walletService.add(walletId, assetSymbol, amount);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Funds added successfully")
                        .content(null)
                        .build());
    }
}
