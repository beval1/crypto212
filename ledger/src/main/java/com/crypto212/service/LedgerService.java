package com.crypto212.service;

import com.crypto212.repository.TransactionRepository;
import com.crypto212.repository.entity.TransactionEntity;
import com.crypto212.service.dto.TransactionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LedgerService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    public LedgerService(TransactionRepository transactionRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    public List<TransactionDTO> getAllTransactions(Long userId) {
        List<TransactionEntity> transactionDTOS = transactionRepository.getAllTransactionsForUser(userId);
        return Arrays.asList(modelMapper.map(transactionDTOS, TransactionDTO[].class));
    }

    public TransactionDTO createTransaction(Long userId, Long walletId, String type, String amount, String assetSymbol) {
        TransactionEntity transactionEntity =
                 transactionRepository.createTransaction(userId, walletId, type, amount, assetSymbol);
        return modelMapper.map(transactionEntity, TransactionDTO.class);
    }
}
