package com.treadingPlatformApplication.repositories;

import com.treadingPlatformApplication.models.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<WalletTransaction,Long> {

    List<WalletTransaction> findByWalletId(Long walletId);

}
