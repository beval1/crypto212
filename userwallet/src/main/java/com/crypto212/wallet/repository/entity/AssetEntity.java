package com.crypto212.wallet.repository.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetEntity {
    private Long id;
    private String assetName;
    private String assetSymbol;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
