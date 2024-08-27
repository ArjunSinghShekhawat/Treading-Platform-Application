package com.treadingPlatformApplication.request;

import lombok.Data;

@Data
public class ResetPasswordReq {
    private String otp;
    private String password;
}
