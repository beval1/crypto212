package com.crypto212.clients.privatewallet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "privatewallet",
        url = "${clients.privatewallet.url}"
)
public interface PrivateWalletClient {

    @GetMapping("api/v1/private-wallet/{assetName}")
    ResponseEntity<AssetBalanceDTO> getWalletAssetAmount(@PathVariable("assetName") String assetName);
}
