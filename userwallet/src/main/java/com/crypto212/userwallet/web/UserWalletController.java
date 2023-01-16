package com.crypto212.userwallet.web;

import com.crypto212.shared.dto.ResponseDTO;
import com.crypto212.userwallet.service.UserWalletService;
import com.crypto212.userwallet.service.dto.WalletDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/userwallet/wallet")
public class UserWalletController {
    private final UserWalletService walletService;

    public UserWalletController(UserWalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getWallet(@RequestHeader("X-User-Id") Long userId) {
        WalletDTO walletDTO = walletService.getWallet(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Wallet fetched successfully!")
                        .content(walletDTO)
                        .build());
    }

    @PreAuthorize("@securityValidationService.isWalletOwner(#userId, #walletId)")
    @PostMapping("/{walletId}/exchange")
    public ResponseEntity<ResponseDTO> exchangeFunds(@RequestHeader("X-User-Id") Long userId,
                                                     @PathVariable("walletId") Long walletId,
                                                     @RequestParam("baseCurrency") String baseCurrency,
                                                     @RequestParam("quoteCurrency") String quoteCurrency,
                                                     @RequestParam("amountToBuy") String amountToBuy,
                                                     @RequestParam("amountToSell") String amountToSell) {
        walletService.exchange(userId, walletId, baseCurrency, quoteCurrency, amountToBuy, amountToSell);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Funds exchanged successfully")
                        .content(null)
                        .build());
    }

    @PreAuthorize("@securityValidationService.isWalletOwner(#userId, #walletId)")
    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<ResponseDTO> withdrawFunds(@RequestHeader("X-User-Id") Long userId,
                                                     @PathVariable("walletId") Long walletId,
                                                     @RequestParam("assetSymbol") String assetSymbol,
                                                     @RequestParam("amount") String amount,
                                                     @RequestParam("address") String toAddress) {
        walletService.withdraw(userId, walletId, assetSymbol, amount, toAddress);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Funds withdrawn successfully")
                        .content(null)
                        .build());
    }

    @PostMapping("/add/{assetSymbol}")
    public ResponseEntity addAssets(@RequestHeader("X-User-Id") Long userId,
                                  @PathVariable("assetSymbol") String assetSymbol,
                                  @RequestParam("amount") String amount) {
        walletService.addAssets(userId, assetSymbol, amount);

        return ResponseEntity
                .status(HttpStatus.OK).build();
    }


}
