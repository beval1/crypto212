package com.crypto212.clients.userwallet;

import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "userwallet",
        url = "${clients.userwallet.url}"
)
public interface UserWalletClient {
    @GetMapping("api/v1/wallet/total-balance/{assetName}")
    public ResponseEntity<TotalUserAssetDTO> totalUserAssetBalance(@PathVariable("assetName") String assetName);

    @PostMapping("api/v1/wallet{walletId}/exchange")
    public ResponseEntity<ResponseDTO> exchangeFunds(@PathVariable("walletId") Long walletId,
                                                     @RequestParam("baseCurrency") String baseCurrency,
                                                     @RequestParam("quoteCurrency") String quoteCurrency,
                                                     @RequestParam("amountToBuy") String amountToBuy,
                                                     @RequestParam("amountToSell") String amountToSell);
}
