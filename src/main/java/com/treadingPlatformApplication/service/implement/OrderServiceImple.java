package com.treadingPlatformApplication.service.implement;

import com.treadingPlatformApplication.domain.OrderType;
import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.models.Order;
import com.treadingPlatformApplication.models.OrderItem;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.repositories.OrderRepository;
import com.treadingPlatformApplication.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderServiceImple implements OrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        return null;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return null;
    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return List.of();
    }

    @Override
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
        return null;
    }
}
