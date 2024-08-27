package com.treadingPlatformApplication.service;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.Withdrawal;
import java.util.List;

public interface WithdrawalService {
    Withdrawal requestWithdrawal(Long amount, User user);
    Withdrawal procedWithwithdrawal(Long withdrawalId,boolean accept);
    List<Withdrawal> getUsersWithdrawalHistory(User user);
    List<Withdrawal>getAllWithdrawalRequest();
}
