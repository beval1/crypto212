package com.crypto212.userwallet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetDTO {
    private Long id;
    private String assetName;
    private String assetSymbol;
}
