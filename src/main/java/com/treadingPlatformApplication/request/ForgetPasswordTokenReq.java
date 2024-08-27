package com.treadingPlatformApplication.request;

import com.treadingPlatformApplication.domain.VerificationType;
import lombok.Data;

@Data
public class ForgetPasswordTokenReq {
    private String sendTo;
    private VerificationType verificationType;
}
