package com.crypto212.payment.web;

import com.crypto212.payment.service.WithdrawService;
import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment/withdraw")
public class CryptoWithdrawController {
    private final WithdrawService withdrawService;

    public CryptoWithdrawController(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @PostMapping("/{assetSymbol}")
    public ResponseEntity<ResponseDTO> withdrawCrypto(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam("walletId") Long walletId,
            @PathVariable("assetSymbol") String assetSymbol,
            @RequestParam("amount") String amount,
            @RequestParam("address") String address
    ){
        withdrawService.sendWithdrawRequest(userId, walletId, assetSymbol, amount, address);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDTO
                .builder()
                        .message("Withdrawn successfully")
                        .content(null)
                .build());
    }
}
