package com.treadingPlatformApplication.service;

import com.treadingPlatformApplication.domain.VerificationType;
import com.treadingPlatformApplication.models.ForgetPassword;
import com.treadingPlatformApplication.models.User;

public interface ForgetPasswordService {

    ForgetPassword createToken(User user, String id, String otp, VerificationType verificationType,String sendTo);
    ForgetPassword findById(String id);
    ForgetPassword findByUser(User user);
    void deleteToken(ForgetPassword token);
}
