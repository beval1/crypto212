package com.crypto212.wallet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class WalletDTO {
    private Long id;
    private Long userId;
    private Set<WalletAssetDTO> walletAssets;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
