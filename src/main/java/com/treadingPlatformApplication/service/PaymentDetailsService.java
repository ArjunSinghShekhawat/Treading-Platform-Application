package com.treadingPlatformApplication.service;
import com.treadingPlatformApplication.models.PaymentDetails;
import com.treadingPlatformApplication.models.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, User user);
    public PaymentDetails getUserPaymentDetails(User user);
}
