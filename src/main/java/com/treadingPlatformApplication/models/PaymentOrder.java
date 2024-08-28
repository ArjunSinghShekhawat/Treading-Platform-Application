package com.treadingPlatformApplication.models;
import com.treadingPlatformApplication.domain.PaymentMethod;
import com.treadingPlatformApplication.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PaymentOrderStatus status;

    private PaymentMethod paymentMethod;

    @ManyToOne
    private User user;
}
