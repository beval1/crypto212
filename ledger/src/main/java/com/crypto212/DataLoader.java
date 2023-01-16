package com.crypto212;

import com.crypto212.repository.TransactionRepository;
import com.crypto212.repository.entity.TransactionType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements ApplicationRunner {
    private final TransactionRepository transactionRepository;

    public DataLoader(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (transactionRepository.getAllTransactionTypes().isEmpty()) {
            Arrays.stream(TransactionType.values()).toList().forEach(v -> {
                transactionRepository.createTransactionType(v.toString());
            });
        }
    }
}
