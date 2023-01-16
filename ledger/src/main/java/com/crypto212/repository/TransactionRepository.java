package com.crypto212.repository;

import com.crypto212.repository.entity.TransactionEntity;
import com.crypto212.repository.entity.TransactionType;

import java.util.List;

public interface TransactionRepository {
    List<TransactionEntity> getAllTransactionsForUser(Long userId);

    TransactionEntity createTransaction(Long userId, Long walletId, String type, String amount, String assetSymbol);
    List<TransactionType> getAllTransactionTypes();
    TransactionType createTransactionType(String name);
}
