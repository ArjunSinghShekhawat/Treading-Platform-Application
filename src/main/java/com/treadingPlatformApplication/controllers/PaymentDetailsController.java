package com.treadingPlatformApplication.controllers;

import com.treadingPlatformApplication.models.PaymentDetails;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.service.PaymentDetailsService;
import com.treadingPlatformApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
@RestController
public class PaymentDetailsController {
    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestBody PaymentDetails paymentDetailsReq,
                                                            @RequestHeader("Authorization") String jwt){

        User user = this.userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = this.paymentDetailsService.addPaymentDetails(
                paymentDetailsReq.getAccountNumber(),
                paymentDetailsReq.getAccountHolderName(),
                paymentDetailsReq.getIfsc(),
                paymentDetailsReq.getBankName(),
                user
        );

        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
    }
    @GetMapping("/api/payment-details")
    public ResponseEntity<PaymentDetails>getUserPaymentDetails(@RequestHeader("Authorization") String jwt){
        User user = this.userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = this.paymentDetailsService.getUserPaymentDetails(user);
        return new ResponseEntity<>(paymentDetails,HttpStatus.OK);
    }
}
