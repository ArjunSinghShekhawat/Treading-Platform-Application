package com.treadingPlatformApplication.repositories;

import com.treadingPlatformApplication.models.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder,Long> {
}