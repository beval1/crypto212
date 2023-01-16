package com.crypto212.clients.privatewallet;

import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "privatewallet",
        url = "${clients.privatewallet.url}"
)
public interface PrivateWalletClient {

    @GetMapping("api/v1/private-wallet/{assetName}")
    ResponseEntity<AssetBalanceDTO> getWalletAssetAmount(@PathVariable("assetName") String assetName);

    @PostMapping("api/v1/private-wallet/withdraw/{assetSymbol}")
    ResponseEntity<ResponseDTO> withdraw(@PathVariable("assetSymbol") String assetSymbol,
                                         @RequestParam("amount") String amount,
                                         @RequestParam("address") String address);
}
