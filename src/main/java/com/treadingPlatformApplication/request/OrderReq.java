package com.treadingPlatformApplication.request;

import com.treadingPlatformApplication.domain.OrderType;
import lombok.Data;

@Data
public class OrderReq {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
