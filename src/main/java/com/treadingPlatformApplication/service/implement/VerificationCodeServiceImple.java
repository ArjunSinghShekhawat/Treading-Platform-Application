package com.treadingPlatformApplication.service.implement;
import com.treadingPlatformApplication.domain.VerificationType;
import com.treadingPlatformApplication.exception.ResourceNotFoundException;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.VerificationCode;
import com.treadingPlatformApplication.repositories.VerificationCodeRepository;
import com.treadingPlatformApplication.service.VerificationCodeService;
import com.treadingPlatformApplication.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeServiceImple implements VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode = new VerificationCode();

        verificationCode.setVerificationType(verificationType);
        verificationCode.setUser(user);
        verificationCode.setOtp(String.valueOf(OtpUtils.generateOTP()));

        return this.verificationCodeRepository.save(verificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) {
        return this.verificationCodeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("verificationCode","id",String.valueOf(id)));
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return this.verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        this.verificationCodeRepository.delete(verificationCode);
    }
}
