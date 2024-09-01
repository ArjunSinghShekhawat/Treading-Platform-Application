package com.treadingPlatformApplication.service;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.treadingPlatformApplication.domain.PaymentMethod;
import com.treadingPlatformApplication.models.PaymentOrder;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.responce.PaymentResponce;

public interface PaymentOrderService {
    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);
    PaymentOrder getPaymentOrderById(Long id);
    Boolean proccedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException;
    PaymentResponce createRazorpayPaymentLink(User user, Long amount, Long id) throws RazorpayException;
    PaymentResponce createStripePaymentLink(User user, Long amount,Long orderId) throws StripeException;
}
