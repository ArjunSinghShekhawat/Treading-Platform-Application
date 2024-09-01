package com.treadingPlatformApplication.controllers;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.treadingPlatformApplication.domain.PaymentMethod;
import com.treadingPlatformApplication.models.PaymentOrder;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.responce.PaymentResponce;
import com.treadingPlatformApplication.service.PaymentOrderService;
import com.treadingPlatformApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentOrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponce> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt
    ) throws RazorpayException, StripeException {
        User user = this.userService.findUserProfileByJwt(jwt);
        PaymentResponce paymentResponce;

        PaymentOrder order = this.paymentOrderService.createOrder(user,amount,paymentMethod);

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            paymentResponce  = this.paymentOrderService.createRazorpayPaymentLink(user,amount,order.getId());
        }
        else{
            paymentResponce = this.paymentOrderService.createStripePaymentLink(user,amount,order.getId());

        }
        return new ResponseEntity<>(paymentResponce, HttpStatus.CREATED);
    }


}
