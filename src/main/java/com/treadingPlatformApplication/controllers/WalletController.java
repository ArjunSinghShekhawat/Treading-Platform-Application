package com.treadingPlatformApplication.controllers;
import com.treadingPlatformApplication.domain.WalletTransactionType;
import com.treadingPlatformApplication.models.*;
import com.treadingPlatformApplication.repositories.TransactionHistoryRepository;
import com.treadingPlatformApplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentOrderService paymentOrderService;


    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    private TransactionHistoryService transactionHistoryService;


    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization")String jwt) throws Exception {
        User user  = this.userService.findUserProfileByJwt(jwt);

        Wallet wallet = this.walletService.getUserWallet(user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet>walletToWalletTransfer(@RequestHeader("Authorization")String jwt,
                                                        @PathVariable Long walletId,
                                                        @RequestBody WalletTransaction req) throws Exception {

        User senderuser  = this.userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = this.walletService.findWalletById(walletId);
        Wallet wallet = this.walletService.walletToWalletTransfer(senderuser,receiverWallet,req.getAmount());


        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setWallet(wallet);
        walletTransaction.setAmount(req.getAmount());
        walletTransaction.setDate(LocalDate.now());
        walletTransaction.setWalletTransactionType(WalletTransactionType.WITHDRAWAL);
        walletTransaction.setTransferId(receiverWallet.getId());
        walletTransaction.setPurpose(req.getPurpose());
        this.transactionHistoryRepository.save(walletTransaction);

        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);

    }
    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet>walletToWalletTransfer(@RequestHeader("Authorization")String jwt, @PathVariable Long orderId) throws Exception {

        User user  = this.userService.findUserProfileByJwt(jwt);
        Order order = this.orderService.getOrderById(orderId);
        Wallet wallet = this.walletService.payOrderPayment(order,user);



        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);

    }
    @PutMapping("/deposit")
    public ResponseEntity<Wallet>addMoneyToWallet(@RequestHeader("Authorization")String jwt,
                                                  @RequestParam(name = "order_id")Long orderId,
                                                  @RequestParam(name = "payment_id") String paymentId) throws Exception {

        User user  = this.userService.findUserProfileByJwt(jwt);

        Wallet wallet = this.walletService.getUserWallet(user);

        PaymentOrder order =  this.paymentOrderService.getPaymentOrderById(orderId);

        Boolean status = this.paymentOrderService.proccedPaymentOrder(order,paymentId);

        if(status){
            wallet = this.walletService.addBalance(wallet,order.getAmount());
        }

        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);

    }
    @GetMapping("/transaction")
    public ResponseEntity<List<WalletTransaction>> getUserTransactionHistory(@RequestHeader("Authorization")String jwt) throws Exception {
        List<WalletTransaction> userTransactionHistory = this.transactionHistoryService.getUserTransactionHistory(jwt);
        return new ResponseEntity<>(userTransactionHistory, HttpStatus.ACCEPTED);
    }

}
