package com.treadingPlatformApplication.service;

import com.treadingPlatformApplication.models.WalletTransaction;

import java.util.List;

public interface TransactionHistoryService {

    List<WalletTransaction> getUserTransactionHistory(String jwt) throws Exception;
}
