package com.crypto212.wallet.repository.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletAssetEntity {
    private AssetEntity assetEntity;
    private BigDecimal amount;
}
