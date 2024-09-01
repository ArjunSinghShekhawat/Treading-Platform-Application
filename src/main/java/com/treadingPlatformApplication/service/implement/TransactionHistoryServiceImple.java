package com.treadingPlatformApplication.service.implement;

import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.Wallet;
import com.treadingPlatformApplication.models.WalletTransaction;
import com.treadingPlatformApplication.repositories.TransactionHistoryRepository;
import com.treadingPlatformApplication.service.TransactionHistoryService;
import com.treadingPlatformApplication.service.UserService;
import com.treadingPlatformApplication.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionHistoryServiceImple implements TransactionHistoryService {

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;


    @Override
    public List<WalletTransaction> getUserTransactionHistory(String jwt) throws Exception {

        User user= this.userService.findUserProfileByJwt(jwt);
        Wallet wallet = this.walletService.getUserWallet(user);
        return this.transactionHistoryRepository.findByWalletId(wallet.getId());
    }
}
