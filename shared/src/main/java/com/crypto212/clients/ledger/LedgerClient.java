package com.crypto212.clients.ledger;

import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ledger",
        url = "${clients.ledger.url}"
)
public interface LedgerClient {
    @PostMapping("api/v1/ledger")
    public ResponseEntity<ResponseDTO> createTransaction(@RequestHeader("X-User-Id") Long userId,
                                                         @RequestParam("type") String type,
                                                         @RequestParam("walletId") Long walletId,
                                                         @RequestParam("amount") String amount,
                                                         @RequestParam("assetSymbol") String assetSymbol);
}
