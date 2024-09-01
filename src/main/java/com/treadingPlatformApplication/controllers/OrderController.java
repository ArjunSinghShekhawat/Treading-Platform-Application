package com.treadingPlatformApplication.controllers;
import com.treadingPlatformApplication.domain.OrderType;
import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.models.Order;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.request.OrderReq;
import com.treadingPlatformApplication.service.CoinService;
import com.treadingPlatformApplication.service.OrderService;
import com.treadingPlatformApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserService userService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody OrderReq req
    ) throws Exception {
        User user = this.userService.findUserProfileByJwt(jwt);
        Coin coin = this.coinService.findById(req.getCoinId());

        Order order = this.orderService.processOrder(coin,req.getQuantity(),req.getOrderType(),user);

        return  new ResponseEntity<>(order, HttpStatus.OK);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<Order>getOrderById(@RequestHeader("Authorization") String jwt,@PathVariable Long orderId) throws Exception {

        if(jwt==null){
            throw new Exception("token missing...");
        }
        User user = this.userService.findUserProfileByJwt(jwt);
        Order order = this.orderService.getOrderById(orderId);

        if(order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }
        else{
            throw new Exception("You do not have access");
        }
    }
    @GetMapping()
    public ResponseEntity<List<Order>>getAllOrderForUser(@RequestHeader("Authorization") String jwt,
                                                         @RequestParam(name="order_type",required = false) OrderType order_type,
                                                         @RequestParam(name="asset_symbol",required = false)String asset_symbol) throws Exception {
        if(jwt==null){
            throw new Exception("token missing...");
        }

        Long userId  = this.userService.findUserProfileByJwt(jwt).getId();
        List<Order>orderList = this.orderService.getAllOrdersOfUser(userId,order_type,asset_symbol);

        return ResponseEntity.ok(orderList);
    }
}
