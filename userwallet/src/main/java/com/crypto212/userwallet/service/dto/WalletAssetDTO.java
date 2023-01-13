package com.crypto212.userwallet.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletAssetDTO {
    private AssetDTO asset;
    private String amount;
}
