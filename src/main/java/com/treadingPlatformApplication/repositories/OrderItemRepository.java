package com.treadingPlatformApplication.repositories;
import com.treadingPlatformApplication.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository  extends JpaRepository<OrderItem,Long> {
}
