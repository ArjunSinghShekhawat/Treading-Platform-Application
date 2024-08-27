package com.treadingPlatformApplication.repositories;

import com.treadingPlatformApplication.models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {

    VerificationCode findByUserId(Long userId);
}
