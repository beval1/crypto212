package com.crypto212.userwallet.web;

import com.crypto212.shared.dto.ResponseDTO;
import com.crypto212.userwallet.service.UserWalletService;
import com.crypto212.userwallet.service.dto.WalletDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/wallet")
public class UserWalletController {
    private final UserWalletService walletService;

    public UserWalletController(UserWalletService walletService) {
        this.walletService = walletService;
    }

    @PreAuthorize("#header == #userId")
    @GetMapping
    public ResponseEntity<ResponseDTO> getWallet(@RequestParam("userId") Long userId,
                                                 @RequestHeader("X-User-Id") Long header) {
        WalletDTO walletDTO = walletService.getWallet(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Wallet fetched successfully!")
                        .content(walletDTO)
                        .build());
    }

    @PostMapping("/{walletId}/exchange")
    public ResponseEntity<ResponseDTO> exchangeFunds(@PathVariable("walletId") Long walletId,
                                                     @RequestParam("baseCurrency") String baseCurrency,
                                                     @RequestParam("quoteCurrency") String quoteCurrency,
                                                     @RequestParam("amountToBuy") String amountToBuy,
                                                     @RequestParam("amountToSell") String amountToSell) {
        walletService.exchange(walletId, baseCurrency, quoteCurrency, amountToBuy, amountToSell);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Funds exchanged successfully")
                        .content(null)
                        .build());
    }


}
