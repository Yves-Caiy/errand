package com.axin.system.service;

import com.axin.system.domain.OrderItem;
import com.axin.system.domain.OrderMain;

import java.util.List;

public interface IOrderService {
    OrderMain selectOrderMainById(Long id);

    List<OrderMain> selectOrderMainList(OrderMain order);

    List<OrderItem> selectOrderItemsByOrderId(Long orderId);

    int insertOrderMain(OrderMain order);

    int updateOrderMain(OrderMain order);

    int insertOrderItem(OrderItem item);

    int acceptOrder(Long id, Long runnerId);
}
