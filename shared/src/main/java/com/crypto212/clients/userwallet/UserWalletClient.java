package com.crypto212.clients.userwallet;

import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "userwallet",
        url = "${clients.userwallet.url}"
)
public interface UserWalletClient {
    @GetMapping("api/v1/assets/{assetName}")
    public ResponseEntity<TotalUserAssetDTO> totalUserAssetBalance(@PathVariable("assetName") String assetName);

    @PostMapping("api/v1/wallet/{walletId}/exchange")
    public ResponseEntity<ResponseDTO> exchangeFunds(@RequestHeader("X-User-Id") Long userId,
                                                     @PathVariable("walletId") Long walletId,
                                                     @RequestParam("baseCurrency") String baseCurrency,
                                                     @RequestParam("quoteCurrency") String quoteCurrency,
                                                     @RequestParam("amountToBuy") String amountToBuy,
                                                     @RequestParam("amountToSell") String amountToSell);
}
