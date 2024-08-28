package com.treadingPlatformApplication.controllers;

import com.treadingPlatformApplication.service.OrderService;
import com.treadingPlatformApplication.service.UserService;
import com.treadingPlatformApplication.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;


}
