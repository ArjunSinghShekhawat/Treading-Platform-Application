package com.treadingPlatformApplication.service;
import com.treadingPlatformApplication.domain.OrderType;
import com.treadingPlatformApplication.models.Coin;
import com.treadingPlatformApplication.models.Order;
import com.treadingPlatformApplication.models.OrderItem;
import com.treadingPlatformApplication.models.User;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId);

    List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;

}