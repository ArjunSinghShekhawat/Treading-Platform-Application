package com.treadingPlatformApplication.service;
import com.treadingPlatformApplication.domain.VerificationType;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);
    VerificationCode getVerificationCodeById(Long id);
    VerificationCode getVerificationCodeByUser(Long userId);
    void deleteVerificationCodeById(VerificationCode verificationCode);
}
