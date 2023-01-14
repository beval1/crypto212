package com.crypto212.userwallet.service;

import com.crypto212.shared.exception.ApiException;
import com.crypto212.userwallet.repository.entity.WalletEntity;
import com.crypto212.userwallet.repository.postgres.PostgresWalletRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SecurityValidationService {
    private final PostgresWalletRepository postgresWalletRepository;

    public SecurityValidationService(PostgresWalletRepository postgresWalletRepository) {
        this.postgresWalletRepository = postgresWalletRepository;
    }

    public boolean isWalletOwner(Long userId, Long walletId){
        //Don't indicate if such wallet/user exists
        WalletEntity walletEntity = postgresWalletRepository.getWallet(userId).orElseThrow(
                () -> new ApiException(HttpStatus.FORBIDDEN, "No permission!"));
        return Objects.equals(walletEntity.getId(), walletId);
    }
}
