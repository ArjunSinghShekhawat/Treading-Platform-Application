package com.treadingPlatformApplication.controllers;

import com.treadingPlatformApplication.domain.VerificationType;
import com.treadingPlatformApplication.models.ForgetPassword;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.VerificationCode;
import com.treadingPlatformApplication.request.ForgetPasswordTokenReq;
import com.treadingPlatformApplication.request.ResetPasswordReq;
import com.treadingPlatformApplication.responce.AuthResponce;
import com.treadingPlatformApplication.responce.ResponceApi;
import com.treadingPlatformApplication.service.ForgetPasswordService;
import com.treadingPlatformApplication.service.UserService;
import com.treadingPlatformApplication.service.VerificationCodeService;
import com.treadingPlatformApplication.service.implement.EmailService;
import com.treadingPlatformApplication.utils.OtpUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ForgetPasswordService forgetPasswordService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;


    @GetMapping("/profile")
    public ResponseEntity<User>getUserProfile(@RequestHeader("Authorization") String jwt){
        User user = this.userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User>getUserProfile(@PathVariable Long userId){
        User user = this.userService.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PostMapping("/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt, @PathVariable VerificationType verificationType) throws MessagingException {
        User user = this.userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = this.verificationCodeService.getVerificationCodeByUser(user.getId());

        if(verificationCode==null){
            verificationCode = this.verificationCodeService.sendVerificationCode(user,verificationType);
        }
        if(verificationType.equals(VerificationType.EMAIL)){
            this.emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }
        return new ResponseEntity<>("Verification Otp Send Successfully", HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt, @PathVariable String otp) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = this.verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ? verificationCode.getEmail() : verificationCode.getMobile();
        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified) {
            User updatedUser = this.userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            this.verificationCodeService.deleteVerificationCodeById(verificationCode);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        throw new Exception("Wrong Otp");
    }
    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponce> sendForgetPasswordOtp(@RequestHeader("Authorization") String jwt, @RequestBody ForgetPasswordTokenReq req) throws MessagingException {

        User user = this.userService.findUserProfileByJwt(jwt);

        String otp = String.valueOf(OtpUtils.generateOTP());
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgetPassword token = this.forgetPasswordService.findByUser(user);
        if(token==null){
            token = this.forgetPasswordService.createToken(user,id,otp,req.getVerificationType(), req.getSendTo());
        }

        if(req.getVerificationType().equals(VerificationType.EMAIL)){
            this.emailService.sendVerificationOtpEmail(user.getEmail(),otp);
        }

        AuthResponce response = new AuthResponce();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ResponceApi> resetPassword(@RequestHeader("Authorization") String jwt, @RequestBody ResetPasswordReq req, @RequestParam String id) throws Exception {
        ForgetPassword forgetPassword= this.forgetPasswordService.findById(id);

        boolean isVerified =  forgetPassword.getOtp().equals(req.getOtp());

        if(isVerified){
            userService.updatePassword(forgetPassword.getUser(),req.getPassword());
            ResponceApi res = new ResponceApi();
            res.setMessage("Password update successfully");
            return  new ResponseEntity<>(res,HttpStatus.OK);
        }
        throw new Exception("Wrong Otp");
    }
}
