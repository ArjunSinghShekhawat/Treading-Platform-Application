package com.treadingPlatformApplication.service;
import com.treadingPlatformApplication.models.Order;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.Wallet;

public interface WalletService {
    Wallet getUserWallet(User user) throws Exception;
    Wallet addBalance(Wallet wallet,Long amount);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(User sender,Wallet receiverWaller,Long amount) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;
}
