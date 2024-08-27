package com.treadingPlatformApplication.repositories;

import com.treadingPlatformApplication.models.Order;

import java.util.List;

public interface OrderRepository {
    List<Order>findByUserId(Long userId);

}
