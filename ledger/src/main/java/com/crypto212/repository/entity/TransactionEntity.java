package com.crypto212.repository.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionEntity {
    private long id;
    private long userId;
    private long walletId;
    private TransactionType transactionType;
    private String assetSymbol;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
