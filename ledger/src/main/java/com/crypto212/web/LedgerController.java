package com.crypto212.web;

import com.crypto212.service.LedgerService;
import com.crypto212.service.dto.TransactionDTO;
import com.crypto212.shared.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ledger")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions(@RequestHeader("X-User-Id") Long userId){
        List<TransactionDTO> transactionDTOS = ledgerService.getAllTransactions(userId);
        return ResponseEntity.status(HttpStatus.OK).body(transactionDTOS);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createTransaction(@RequestHeader("X-User-Id") Long userId,
                                            @RequestParam("type") String type,
                                            @RequestParam("walletId") Long walletId,
                                            @RequestParam("amount") String amount,
                                            @RequestParam("assetSymbol") String assetSymbol){
        TransactionDTO transactionDTO = ledgerService.createTransaction(userId, walletId, type, amount, assetSymbol);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDTO
                        .builder()
                        .content(transactionDTO)
                        .build());
    }
}
