package com.treadingPlatformApplication.service.implement;

import com.treadingPlatformApplication.domain.VerificationType;
import com.treadingPlatformApplication.exception.ResourceNotFoundException;
import com.treadingPlatformApplication.models.ForgetPassword;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.repositories.ForgetPasswordRepository;
import com.treadingPlatformApplication.service.ForgetPasswordService;
import com.treadingPlatformApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForgetPasswordServiceImple implements ForgetPasswordService {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgetPasswordRepository forgetPasswordRepository;


    @Override
    public ForgetPassword createToken(User user, String id, String otp, VerificationType verificationType, String sendTo) {

        ForgetPassword forgetPassword = new ForgetPassword();
        forgetPassword.setOtp(otp);
        forgetPassword.setUser(user);
        forgetPassword.setSendTo(sendTo);
        forgetPassword.setVerificationType(verificationType);
        forgetPassword.setId(id);
        return this.forgetPasswordRepository.save(forgetPassword);
    }

    @Override
    public ForgetPassword findById(String id) {
        return this.forgetPasswordRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("forgetPassword","id",id));
    }

    @Override
    public ForgetPassword findByUser(User user) {
        return this.forgetPasswordRepository.findByUserId(user.getId());
    }

    @Override
    public void deleteToken(ForgetPassword token) {
       this.forgetPasswordRepository.delete(token);
    }
}
