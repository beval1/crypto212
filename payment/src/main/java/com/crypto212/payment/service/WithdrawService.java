package com.crypto212.payment.service;

import com.crypto212.clients.userwallet.UserWalletClient;
import org.springframework.stereotype.Service;

@Service
public class WithdrawService {
    private final UserWalletClient userWalletClient;

    public WithdrawService(UserWalletClient userWalletClient) {
        this.userWalletClient = userWalletClient;
    }

    public void sendWithdrawRequest(Long userId, Long walletId, String assetSymbol, String amount, String address){
        //TODO: implement message queue
        userWalletClient.withdrawFunds(userId, walletId, assetSymbol, amount, address);
    }
}
