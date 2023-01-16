package com.crypto212.service.dto;

import com.crypto212.repository.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TransactionDTO {
    private long id;
    private long userId;
    private long walletId;
    private TransactionType transactionType;
    private String assetSymbol;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
