package com.treadingPlatformApplication.models;

import com.treadingPlatformApplication.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled=true;
    private VerificationType verificationType;
}
