package com.treadingPlatformApplication.controllers;

import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.models.Wallet;
import com.treadingPlatformApplication.models.Withdrawal;
import com.treadingPlatformApplication.service.UserService;
import com.treadingPlatformApplication.service.WalletService;
import com.treadingPlatformApplication.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WithdrawalController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private UserService userService;


    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(@PathVariable Long amount, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Wallet userWallet = this.walletService.getUserWallet(user);

        Withdrawal withdrawal = this.withdrawalService.requestWithdrawal(amount,user);
        this.walletService.addBalance(userWallet,-withdrawal.getAmount());

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }
    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?>proceeWithdrawal(@PathVariable Long id,@PathVariable boolean accept,
                                             @RequestHeader("Authorization") String jwt) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal = this.withdrawalService.procedWithwithdrawal(id,accept);


        Wallet userWallet = this.walletService.getUserWallet(user);
        if(!accept){
            this.walletService.addBalance(userWallet,withdrawal.getAmount());
        }
        return  new ResponseEntity<>(withdrawal,HttpStatus.OK);
    }
    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>>getWithdrawalHistory(@RequestHeader("Authorization") String jwt){
        User user = this.userService.findUserProfileByJwt(jwt);
        List<Withdrawal>withdrawalHistory = this.withdrawalService.getUsersWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawalHistory,HttpStatus.OK);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>>getAllWithdrawalRequest(@RequestHeader("Authorization") String jwt){
        User user = this.userService.findUserProfileByJwt(jwt);
        List<Withdrawal>withdrawalRequestList = this.withdrawalService.getAllWithdrawalRequest();
        return new ResponseEntity<>(withdrawalRequestList,HttpStatus.OK);
    }
}
