package com.treadingPlatformApplication.service;

import com.treadingPlatformApplication.models.TwoFactorOtp;
import com.treadingPlatformApplication.models.User;

public interface TwoFactorOtpService {

    TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt);
    TwoFactorOtp findByUserId(Long userId);
    TwoFactorOtp findById(String id);
    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp,String otp);
    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}
