package com.crypto212.wallet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AssetDTO {
    private Long id;
    private String assetName;
    private String assetSymbol;
}
