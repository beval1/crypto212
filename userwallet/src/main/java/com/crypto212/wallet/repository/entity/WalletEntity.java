package com.crypto212.wallet.repository.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {
    private Long id;
    private Long userId;
    private Set<WalletAssetEntity> walletAssets;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
