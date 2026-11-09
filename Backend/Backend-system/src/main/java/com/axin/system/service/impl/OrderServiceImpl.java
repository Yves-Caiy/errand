package com.axin.system.service.impl;

import com.axin.system.domain.OrderItem;
import com.axin.system.domain.OrderMain;
import com.axin.system.mapper.OrderItemMapper;
import com.axin.system.mapper.OrderMainMapper;
import com.axin.system.service.IOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderMainMapper orderMainMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderServiceImpl(OrderMainMapper orderMainMapper, OrderItemMapper orderItemMapper) {
        this.orderMainMapper = orderMainMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public OrderMain selectOrderMainById(Long id) {
        return orderMainMapper.selectOrderMainById(id);
    }

    @Override
    public List<OrderMain> selectOrderMainList(OrderMain order) {
        return orderMainMapper.selectOrderMainList(order);
    }

    @Override
    public List<OrderItem> selectOrderItemsByOrderId(Long orderId) {
        return orderItemMapper.selectOrderItemListByOrderId(orderId);
    }

    @Override
    public int insertOrderMain(OrderMain order) {
        return orderMainMapper.insertOrderMain(order);
    }

    @Override
    public int updateOrderMain(OrderMain order) {
        return orderMainMapper.updateOrderMain(order);
    }

    @Override
    public int insertOrderItem(OrderItem item) {
        return orderItemMapper.insertOrderItem(item);
    }

    @Override
    public int acceptOrder(Long id, Long runnerId) {
        return orderMainMapper.acceptOrder(id, runnerId);
    }
}
